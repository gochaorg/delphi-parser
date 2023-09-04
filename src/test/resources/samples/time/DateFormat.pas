unit DateFormat;

// Заготовка для парсинга форматы даты

interface

uses
  MyDate,
  Validation,
  Logging, Loggers,
  IntegerList,
  Classes,
  Config,

  SysUtils;

type

IMyDateFormat = interface
  function parse(
    str:WideString;
    from:Integer;
    var date:TMyDate;
    var validation:TDataValidation;
    var nextFrom:Integer
  ): boolean;

  procedure build( var str:WideString; var myDate: TMyDate );
end;

TMyDateFormatPlainText = class(TInterfacedObject,IMyDateFormat)
  private
    plainText: WideString;
  public
    constructor Create(plainText:WideString);
    destructor Destroy; override;

    function parse(
      str:WideString;
      from:Integer;
      var date:TMyDate;
      var validation:TDataValidation;
      var nextFrom:Integer
    ): boolean;

    procedure build( var str:WideString; var myDate: TMyDate );
end;

TMyDateFormatNumber = class(TInterfacedObject,IMyDateFormat)
  private
    digitCountMin:Integer;
    digitCountMax:Integer;
  public
    constructor Create(digitCountMin:Integer;digitCountMax:Integer);
    destructor Destroy; override;

    function parse(
      str:WideString;
      from:Integer;
      var date:TMyDate;
      var validation:TDataValidation;
      var nextFrom:Integer
    ): boolean;

    function numberParsed(
      num:Integer;
      var date:TMyDate;
      var validation:TDataValidation
    ):boolean; virtual;

    procedure build( var str:WideString; var myDate: TMyDate ); virtual;
end;

TMyDateFormatYear = class(TMyDateFormatNumber)
  public
    constructor Create;
    destructor Destroy; override;

    function numberParsed(
      num:Integer;
      var date:TMyDate;
      var validation:TDataValidation
    ):boolean; override;

    procedure build( var str:WideString; var myDate: TMyDate ); override;
end;

TMyDateFormatMonth = class(TMyDateFormatNumber)
  public
    constructor Create;
    destructor Destroy; override;

    function numberParsed(
      num:Integer;
      var date:TMyDate;
      var validation:TDataValidation
    ):boolean; override;

    procedure build( var str:WideString; var myDate: TMyDate ); override;
end;

TMyDateFormatMonthsDay = class(TMyDateFormatNumber)
  public
    constructor Create;
    destructor Destroy; override;

    function numberParsed(
      num:Integer;
      var date:TMyDate;
      var validation:TDataValidation
    ):boolean; override;

    procedure build( var str:WideString; var myDate: TMyDate ); override;
end;

// Валидация даты (год, месяц, день) на
//   месяц от 1 до 12
//   день от 1 до 31
//   день с учетом вискосного года
TMyDateFormatValidateDate = class(TInterfacedObject,IMyDateFormat)
  public
    constructor Create;
    destructor Destroy; override;
    function parse(
      str:WideString;
      from:Integer;
      var date:TMyDate;
      var validation:TDataValidation;
      var nextFrom:Integer
    ): boolean;
    procedure build( var str:WideString; var myDate: TMyDate );
end;

// Несколько компонент времени собранных в последовательность
IMyDateFormatSequence = interface(IMyDateFormat)
  procedure addPlain( str: WideString );
  procedure addYear;
  procedure addMonth;
  procedure addMonthsDay;
  procedure addValidateDate;
end;

TMyDateFormatSequence = class(TInterfacedObject,IMyDateFormat,IMyDateFormatSequence)
  private
    dateComponents : TList; // of TComponentHolder
  public
    constructor Create;
    destructor Destroy; override;

    function parse(
      str:WideString;
      from:Integer;
      var date:TMyDate;
      var validation:TDataValidation;
      var nextFrom:Integer
    ): boolean;

    procedure build( var str:WideString; var myDate: TMyDate );

    procedure addPlain( str: WideString );
    procedure addYear;
    procedure addMonth;
    procedure addMonthsDay;
    procedure addValidateDate;
end;

// Парсинг формата времени
//   str - строка формата
//     %Y - год 4 цифры
//     %M - месяц 2 цифры
//        01 - январь
//     %D - день месяца 2 цифры
//     %% - символ %
function DateFormatParse( str:WideString ):IMyDateFormat;

implementation

var
  log: ILog;

{ TMyDateFormatPlainText }

// Добавление нулей слева до необходимой длинны
function PadNumber(str:WideString; len:Integer):WideString;
var
  i:Integer;
begin
  result := str;
  if length(str) < len then
  begin
    for i:=1 to (len - length(str)) do begin
      result := '0' + result;
    end;
  end;
end;

constructor TMyDateFormatPlainText.Create(plainText: WideString);
begin
  inherited Create;
  self.plainText := plainText;
end;

destructor TMyDateFormatPlainText.Destroy;
begin
  inherited Destroy;
end;

procedure TMyDateFormatPlainText.build(var str: WideString; var myDate: TMyDate);
begin
  str := str + self.plainText;
end;


function TMyDateFormatPlainText.parse(
  str: WideString;
  from: Integer;
  var date: TMyDate;
  var validation: TDataValidation;
  var nextFrom: Integer): boolean;
var
  subStr: WideString;
begin
  result := false;

  if ((from-1) > (length(str) - length(self.plainText)) ) then begin
    validation.addError(
      'Нет входных данных, from='+IntToStr(from)+
      ' length='+IntToStr(length(str))
    );
  end else begin
    subStr := Copy(str, from, length(self.plainText));
    if subStr = self.plainText then begin
      result := true;
      nextFrom := from + length(self.plainText);
    end else begin
      validation.addError(
        'Ожидалось '''+self.plainText+
        ''' но встретилось: '''+subStr+'''' );
    end;
  end;
end;

{ TMyDateFormatNumber }

constructor TMyDateFormatNumber.Create(digitCountMin:Integer;digitCountMax:Integer);
begin
  inherited Create;
  self.digitCountMin := digitCountMin;
  self.digitCountMax := digitCountMax;
end;

destructor TMyDateFormatNumber.Destroy;
begin
  inherited Destroy;
end;

function TMyDateFormatNumber.numberParsed(
  num: Integer;
  var date:TMyDate;
  var validation: TDataValidation): boolean;
begin
  result := true;
end;

procedure TMyDateFormatNumber.build(
  var str: WideString;
  var myDate: TMyDate
);
begin

end;

function TMyDateFormatNumber.parse(
  str: WideString;
  from: Integer;
  var date: TMyDate;
  var validation: TDataValidation;
  var nextFrom: Integer): boolean;

var
  ptr : Integer;
  state : Integer;
  digits : TIntegerList;
  digit : Integer;
  num : Integer;
  numPow: Integer;
  i: Integer;

  function getDigit: Integer;
  begin
    result := -1;
    if str[ptr] = WideChar('0') then result := 0
    else if str[ptr] = WideChar('1') then result := 1
    else if str[ptr] = WideChar('2') then result := 2
    else if str[ptr] = WideChar('3') then result := 3
    else if str[ptr] = WideChar('4') then result := 4
    else if str[ptr] = WideChar('5') then result := 5
    else if str[ptr] = WideChar('6') then result := 6
    else if str[ptr] = WideChar('7') then result := 7
    else if str[ptr] = WideChar('8') then result := 8
    else if str[ptr] = WideChar('9') then result := 9;
  end;

  
begin
  result := false;
  digits := TIntegerList.Create;
  try
    if (from > length(str)) or (from < 1) then begin
      validation.addError('Нет входных данных начиная с позиции '+IntToStr(from));
    end else begin
      ptr := from;
      state := 0;
      while state >= 0 do begin
        if ptr > length(str) then state := -1
        else if (digits.Count >= self.digitCountMax) and (self.digitCountMax > 0) then state := -2
        else begin
          digit := getDigit;
          if digit < 0 then begin
            state := -3;          
          end else begin
            digits.add( digit );
            ptr := ptr + 1;
          end;
        end;
      end;

      if ((digits.Count >= self.digitCountMin) and (self.digitCountMin>0)) or (self.digitCountMin <= 0) then begin
        num := 0;
        numPow := 1;
        for i := (digits.Count-1) downto 0 do begin 
          digit := digits.Items[i];
          num := num + digit * numPow;
          numPow := numPow * 10;
        end;
        if self.numberParsed(num, date, validation) then begin
          nextFrom := ptr;
          result := true;
        end;
      end else begin
        validation.addError('Введено малое кол-во цифр');
      end;
    end;
  finally
    digits.Destroy;
  end;  
end;

{ TMyDateFormatYear }

constructor TMyDateFormatYear.Create;
begin
  inherited Create(4,4)
end;

destructor TMyDateFormatYear.Destroy;
begin
  inherited Destroy;
end;

procedure TMyDateFormatYear.build(var str: WideString;
  var myDate: TMyDate);
begin
  str := str + PadNumber(IntToStr(myDate.year),4);
end;

function TMyDateFormatYear.numberParsed(
  num: Integer;
  var date:TMyDate;
  var validation: TDataValidation): boolean;
begin
  date.year := num;
  result := true;
end;

/////////////////////

constructor TMyDateFormatMonth.Create;
begin
  inherited Create(2,2)
end;

destructor TMyDateFormatMonth.Destroy;
begin
  inherited Destroy;
end;

procedure TMyDateFormatMonth.build(
  var str: WideString;
  var myDate: TMyDate);
begin
  str := str + PadNumber(IntToStr(myDate.month),2);
end;

function TMyDateFormatMonth.numberParsed(
  num: Integer;
  var date:TMyDate;
  var validation: TDataValidation): boolean;
begin
  date.month := num;
  result := true;
end;

/////////////////////

constructor TMyDateFormatMonthsDay.Create;
begin
  inherited Create(2,2)
end;

destructor TMyDateFormatMonthsDay.Destroy;
begin
  inherited Destroy;
end;

procedure TMyDateFormatMonthsDay.build(
  var str: WideString;
  var myDate: TMyDate);
begin
  str := str + PadNumber(IntToStr(myDate.date),2);
end;

function TMyDateFormatMonthsDay.numberParsed(
  num: Integer;
  var date:TMyDate;
  var validation: TDataValidation): boolean;
begin
  date.date := num;
  result := true;
end;

{ TMyDateFormatSequence }

type
  TComponentHolder = class
    private
      ref: IMyDateFormat;
    constructor Create(ref:IMyDateFormat);
    destructor Destroy; override;
    function getRef: IMyDateFormat;
  end;

constructor TComponentHolder.Create(ref:IMyDateFormat);
begin
  self.ref := ref;
end;

destructor TComponentHolder.Destroy;
begin
  self.ref := nil;
end;

function TComponentHolder.getRef: IMyDateFormat;
begin
  result := self.ref;
end;

//////////.....///////////
constructor TMyDateFormatSequence.Create;
begin
  inherited Create;
  self.dateComponents := TList.Create;
end;

destructor TMyDateFormatSequence.Destroy;
var
  i:Integer;
  c:TComponentHolder;
begin
  if assigned(self.dateComponents) then begin
    for i:=0 to (self.dateComponents.Count-1) do begin
      c := self.dateComponents[i];
      if assigned(c) then c.Destroy;
    end;
    self.dateComponents.Clear;
    FreeAndNil(self.dateComponents);
  end;
  inherited Destroy;
end;

procedure TMyDateFormatSequence.addMonth;
var
  c:IMyDateFormat;
begin
  c := TMyDateFormatMonth.Create;
  self.dateComponents.Add(TComponentHolder.Create(c));
end;

procedure TMyDateFormatSequence.addPlain(str: WideString);
var
  c:IMyDateFormat;
begin
  c := TMyDateFormatPlainText.Create(str);
  self.dateComponents.Add(TComponentHolder.Create(c));
end;

procedure TMyDateFormatSequence.addYear;
var
  c:IMyDateFormat;
begin
  c := TMyDateFormatYear.Create;
  self.dateComponents.Add(TComponentHolder.Create(c));
end;

procedure TMyDateFormatSequence.addMonthsDay;
var
  c:IMyDateFormat;
begin
  c := TMyDateFormatMonthsDay.Create;
  self.dateComponents.Add(TComponentHolder.Create(c));
end;


procedure TMyDateFormatSequence.addValidateDate;
var
  c:IMyDateFormat;
begin
  c := TMyDateFormatValidateDate.Create;
  self.dateComponents.Add(TComponentHolder.Create(c));
end;

procedure TMyDateFormatSequence.build(
  var str: WideString;
  var myDate: TMyDate
);
var
  i: Integer;
  c: TComponentHolder;
begin
  for i:=0 to (self.dateComponents.Count-1) do begin
    c := self.dateComponents[i];
    if assigned(c) then begin
      c.getRef.build(str, myDate);
    end;
  end;
end;

function TMyDateFormatSequence.parse(
  str: WideString;
  from: Integer;
  var date: TMyDate;
  var validation: TDataValidation;
  var nextFrom: Integer): boolean;
var
  i:Integer;
  c:TComponentHolder;
  ptr:Integer;
  next:Integer;
begin
  result := true;
  ptr := from;
  for i:=0 to (self.dateComponents.Count-1) do begin
    c := self.dateComponents[i];
    if assigned(c) then begin
      if c.ref.parse(str,ptr,date,validation,next) then begin
        ptr := next;
        nextFrom := next;
      end else begin
        result := false;
      end;
    end;
  end;
end;

{ TMyDateFormatValidateDate }

// Является ли год високосным ?
function isLeapYear(year:Integer):boolean;
begin
  result := false;
  if (year mod 400) = 0 then result := true
  else if (year mod 100) = 0 then result := false
  else if (year mod 4) = 0 then result := true;
end;

// Кол-во дней в месяце
function getMonthLen(year,mon:Integer):Integer;
begin
  result := 0;
  if mon = 1 then result := 31
  else if (mon = 2) and (isLeapYear(year)) then result := 29
  else if (mon = 2) and (not isLeapYear(year)) then result := 28
  else if mon = 3  then result := 31
  else if mon = 4  then result := 30
  else if mon = 5  then result := 31
  else if mon = 6  then result := 30
  else if mon = 7  then result := 31
  else if mon = 8  then result := 31
  else if mon = 9  then result := 30
  else if mon = 10 then result := 31
  else if mon = 11 then result := 30
  else if mon = 12 then result := 31;
end;


procedure TMyDateFormatValidateDate.build(var str: WideString;
  var myDate: TMyDate);
begin
end;

constructor TMyDateFormatValidateDate.Create;
begin
  inherited Create;
end;

destructor TMyDateFormatValidateDate.Destroy;
begin
  inherited Destroy;
end;

function TMyDateFormatValidateDate.parse(
  str: WideString; 
  from: Integer;
  var date: TMyDate; 
  var validation: TDataValidation;
  var nextFrom: Integer): boolean;
begin
  result := true;
  if date.month < 1 then begin
    validation.addError('Номер месяца не модет быть меньше 1');
    result := false;
  end else if date.month > 12 then begin
    validation.addError('Номер месяца не модет быть больше 12');
    result := false;
  end else if date.year < 0 then begin
    validation.addError('Номер года не модет быть меньше 0');
    result := false;
  end else if date.date < 1 then begin
    validation.addError('Дата (день) не может быть меньше 1');
    result := false;
  end else if date.date > getMonthLen(date.year, date.month) then begin
    validation.addError(
      'Дата (день) не может быть больше '+IntToStr(getMonthLen(date.year, date.month))+
      ' для '+IntToStr(date.year)+' года '+IntToStr(date.month)+' месяца');
    result := false;
  end;

end;

// Парсинг формата времени
//   str - строка формата
//     %Y - год 4 цифры
//     %M - месяц 2 цифры
//        01 - январь
//     %D - день месяца 2 цифры
//     %% - символ %
function DateFormatParse( str:WideString ):IMyDateFormat;
var
  state : Integer;
  ptr : Integer;
  fmt : IMyDateFormatSequence;
  buff : WideString;
  procedure flush;
  begin
    if length(buff) > 0 then begin
      fmt.addPlain(buff);
      buff := '';
    end;
  end;
begin
  ptr := 1;
  state := 0;
  fmt := TMyDateFormatSequence.Create;
  result := fmt;
  buff := '';

  while (state >= 0) do begin
    if ptr > length(str) then begin
      state := -1;
    end else if state = 0 then begin
      if str[ptr] = WideChar('%') then begin
        ptr := ptr + 1;
        state := 1;
      end else begin
        buff := buff + str[ptr];
        ptr := ptr + 1;
      end;
    end else if state = 1 then begin
      if str[ptr] = WideChar('%') then begin
        state := 0;
        ptr := ptr + 1;
        buff := buff + '%';
      end else if str[ptr] = WideChar('Y') then begin
        state := 0;
        ptr := ptr + 1;
        flush;
        fmt.addYear;
      end else if str[ptr] = WideChar('M') then begin
        state := 0;
        ptr := ptr + 1;
        flush;
        fmt.addMonth;
      end else if str[ptr] = WideChar('D') then begin
        state := 0;
        ptr := ptr + 1;
        flush;
        fmt.addMonthsDay;
      end else begin
        state := 0;
        ptr := ptr + 1;
        buff := buff + WideChar('%') + str[ptr];
      end;
    end;
  end;

  flush;
  fmt.addValidateDate;
end;

////////////////////////

procedure test;
var
  plain : TMyDateFormatPlainText;
  number : TMyDateFormatNumber;
  year : TMyDateFormatYear;
  month : TMyDateFormatMonth;
  seq : TMyDateFormatSequence;
  fmt : IMyDateFormat;

  myDate : TMyDate;
  nextFrom : Integer;
  validation : TDataValidation;

  generatedString: WideString;

begin
  if applicationConfigObj.isDebug then begin
    log.println('test !');

    plain := TMyDateFormatPlainText.Create('hello');
    number := TMyDateFormatNumber.Create(0,4);
    year := TMyDateFormatYear.Create;
    month := TMyDateFormatMonth.Create;
    seq := TMyDateFormatSequence.Create;

    validation := TDataValidation.Create;
    myDate := TMyDate.Create(0,0,0);

    try
      log.println('parse by format');
      fmt := DateFormatParse('%Y %M %D');
      if fmt.parse('1987 10 22',1,myDate,validation,nextFrom) then begin
        log.println('parsed');
        log.println('  year='+IntToStr(myDate.year));
        log.println('  month='+IntToStr(myDate.month));
        log.println('  date='+IntToStr(myDate.date));
        log.println('  nextFrom='+IntToStr(nextFrom));

        generatedString := '';
        fmt.build(generatedString, myDate);
        log.println('  generated string='+generatedString);
      end else begin
        log.println('not parsed');
        log.println(validation.getMessage);
      end;
      fmt := nil;


      log.println('plain test');
      if plain.parse('hello',1,myDate,validation,nextFrom) then begin
        log.println('parsed');
        log.println('  nextFrom='+IntToStr(nextFrom));
      end else begin
        log.println('not parsed');
      end;

      log.println('number test');
      if number.parse('123456',1,myDate,validation,nextFrom) then begin
        log.println('parsed');
        log.println('  nextFrom='+IntToStr(nextFrom));
      end else begin
        log.println('not parsed');
      end;

      log.println('year test');
      if year.parse('123456',1,myDate,validation,nextFrom) then begin
        log.println('parsed');
        log.println('  year='+IntToStr(myDate.year));
        log.println('  nextFrom='+IntToStr(nextFrom));

        generatedString := '';
        year.build(generatedString, myDate);
        log.println('  generated string='+generatedString);
      end else begin
        log.println('not parsed');
      end;

      log.println('month test');
      if month.parse('123456',1,myDate,validation,nextFrom) then begin
        log.println('parsed');
        log.println('  month='+IntToStr(myDate.month));
        log.println('  nextFrom='+IntToStr(nextFrom));

        generatedString := '';
        month.build(generatedString, myDate);
        log.println('  generated string='+generatedString);
      end else begin
        log.println('not parsed');
      end;

      log.println('seq test');
      seq.addYear;
      seq.addPlain(' ');
      seq.addMonth;

      if seq.parse('1987 10',1,myDate,validation,nextFrom) then begin
        log.println('parsed');
        log.println('  year='+IntToStr(myDate.year));
        log.println('  month='+IntToStr(myDate.month));
        log.println('  nextFrom='+IntToStr(nextFrom));

        generatedString := '';
        seq.build(generatedString, myDate);
        log.println('  generated string='+generatedString);
      end else begin
        log.println('not parsed');
      end;

    finally
      plain.Destroy;
      validation.Destroy;
      myDate.Destroy;
      number.Destroy;
      year.Destroy;
      month.Destroy;
      seq.Destroy;
    end;
  end;
end;

initialization
  log := logger('DateFormat');
  rootLog.addListener(test);


end.
 