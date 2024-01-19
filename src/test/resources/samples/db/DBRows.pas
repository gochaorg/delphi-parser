unit DBRows;

interface

uses
  Classes, SysUtils,

  Map, Loggers, Logging,
  DBRowPredicate;

type

  // Функция прнимающая строку
  TDataRowConsumer  = procedure (row:TStringMap) of object;
  TDataRowConsumerI = procedure (row:IStringMap) of object;

  // Опиcание колонки
  IDBRowColumn = interface
    // Свойство Name - имя колонки
    procedure SetName( str:WideString );
    function  GetName: WideString;
    property Name: WideString read GetName write SetName;

    // Свойство Title - заголовок колонки, отображаемый текст
    procedure SetTitle( str:WideString );
    function  GetTitle: WideString;
    property  Title:WideString read GetTitle write SetTitle;

    // Свойство Visible - видима колонка или нет
    procedure SetVisible( visible:boolean );
    function  GetVisible:boolean;
    property  Visible:boolean read GetVisible write SetVisible;

    // Свойство Width - ширина колонки
    procedure SetWidth( width:Integer );
    function  GetWidth: Integer;
    property  Width:Integer read GetWidth write SetWidth;
  end;

  // Реализация IDBRowColumn
  TDBRowColumn = class(TStringMap,IDBRowColumn,IStringMap)
  published
    constructor Create;
    constructor Copy( sample:TStringMap );
    destructor Destroy; override;

    procedure SetName( str:WideString );
    function GetName: WideString;
    property Name: WideString read GetName write SetName;

    procedure SetTitle( str:WideString );
    function  GetTitle: WideString;
    property  Title:WideString read GetTitle write SetTitle;

    procedure SetVisible( visible:boolean );
    function  GetVisible:boolean;
    property  Visible:boolean read GetVisible write SetVisible;

    procedure SetWidth( width:Integer );
    function  GetWidth: Integer;
    property  Width:Integer read GetWidth write SetWidth;

    procedure CopyFrom( sample:TStringMap );
  end;

  /////////////////////////////////////////////////////////////
  // Выборка строк
  IDBRows = interface
    // Получение колонки (или создание) по имени
    // Аргументы
    //   name - имя колонки
    function AddOrGetColumn( name:WideString ):TDBRowColumn;

    // Получение колонки по имени
    // Аргументы
    //   name - имя колонки
    //   ссылка на колонку
    // Результат
    //   true - ссылка на колонку содержит актуальное значение
    //   false - колонка не создана, ссылка на колонку не актуальна
    function GetColumn( name:WideString; out column:TDBRowColumn ):boolean; overload;

    // Получение колонки по индексу
    // Аргументы
    //   index - индекс колонки (0 - первая колонка)
    //   ссылка на колонку
    // Результат
    //   true - ссылка на колонку содержит актуальное значение
    //   false - колонка не создана, ссылка на колонку не актуальна
    function GetColumn( index:Integer; out column:TDBRowColumn ):boolean; overload;

    // Возвращает кол-во колонок
    function GetColumnsCount: Integer;

    // Возвращает кол-во строк в выборке
    function GetCount: Integer;

    // Возвращает строку по индексу
    function GetItem(index:Integer; out item:TStringMap): boolean;

    // Обход всех строк в выборке и передача каждой в приемник
    // Аргументы
    //   consumer - применик
    procedure Each( consumer:TDataRowConsumer );

    // Добавляет строку в выборку
    procedure Add(row:TStringMap);

    // Удаляет строки кроме указанныъ
    // Аргументы
    //   predicate - указывает условие (предикат) какие строки надо оставить
    procedure Retain( predicate: IDataRowPredicate );
  end;

  // Реализация IDBRows
  TDBRows = class(TInterfacedObject,IDBRows)
  private
    list: TList; // коллекция строк TStringMap
    header: TList; // коллекция колонок TStringMap
  public
    constructor Create;
    destructor Destroy; override;

    function AddOrGetColumn(name: WideString): TDBRowColumn;
    function GetColumn( name:WideString; out column:TDBRowColumn ):boolean; overload;
    function GetColumn( index:Integer; out column:TDBRowColumn ):boolean; overload;
    function GetColumnsCount: Integer;

    procedure Each( consumer:TDataRowConsumer );
    function GetCount: Integer;
    function GetItem(index:Integer; out item:TStringMap): boolean;
    procedure Add(row:TStringMap);
    procedure Retain( predicate: IDataRowPredicate );
  end;

  EIndexOutOfBound = class(Exception);


implementation

var
log : ILog;

{ TDBRows }

constructor TDBRows.Create;
begin
  inherited Create;
  self.list := TList.Create;
  self.header := TList.Create;
end;

destructor TDBRows.Destroy;
var
  i: Integer;
  row: TStringMap;
  hdr: TStringMap;
begin
  for i:=0 to self.list.Count-1 do begin
    row := self.list.Items[i];
    FreeAndNil(row);
  end;
  list.Clear;
  FreeAndNil(list);

  for i:=0 to self.header.Count-1 do begin
    hdr := self.header.Items[i];
    FreeAndNil(hdr);
  end;
  self.header.Clear;
  FreeAndNil(self.header);

  inherited Destroy;
end;

procedure TDBRows.Add(row: TStringMap);
var
  rowCopy: TStringMap;
begin
  rowCopy := TStringMap.Copy(row);
  list.Add( rowCopy );
end;

function TDBRows.GetCount: Integer;
begin
  result := list.Count;
end;

function TDBRows.GetItem(index: Integer; out item:TStringMap ): boolean;
begin
  if (index>=0) and (index<list.Count) then begin
    item := list[index];
    result := true;
  end else begin
    result := false;
    log.println(
      '! TDBRows.GetItem( '+IntToStr(index)+' )'+
      ' index out of range'
    );
  end;
end;

procedure TDBRows.Each(consumer: TDataRowConsumer);
var
  i:Integer;
  row: TStringMap;
begin
  for i:=0 to (list.Count-1) do begin
    row := list[i];
    consumer(row);
  end;
end;

procedure TDBRows.Retain( predicate: IDataRowPredicate );
var
  i:Integer;
  row: TStringMap;
begin
  for i:=(list.Count-1) downto 0 do begin
    row := list[i];
    if not predicate.test(row) then begin
      list.Delete(i);
      FreeAndNil(row);
    end;
  end;
end;


function TDBRows.AddOrGetColumn(name: WideString): TDBRowColumn;
var
  i: Integer;
  hdr: TDBRowColumn;
  found: boolean;
begin
  found := false;
  for i:=0 to (self.header.Count-1) do begin
    hdr := self.header.items[i];
    if assigned(hdr) and (hdr.GetName = name) then begin
      result := hdr;
      found := true;
    end;
  end;

  if not found then begin
    hdr := TDBRowColumn.Create;
    hdr.Name := name;
    self.header.Add(hdr);
    result := hdr;
  end;
end;

function TDBRows.GetColumn(
  name: WideString;
  out column: TDBRowColumn): boolean;
var
  i: Integer;
  hdr: TDBRowColumn;
begin
  result := false;
  for i:=0 to (self.header.Count-1) do begin
    hdr := self.header[i];
    if assigned(hdr) and (hdr.GetName = name) then begin
      result := true;
      column := hdr;
    end;
  end;
end;

function TDBRows.GetColumn(
  index: Integer;
  out column: TDBRowColumn): boolean;
var
  hdr: TDBRowColumn;
begin
  result := false;
  if (index>=0) and (index<self.header.Count) then begin
    hdr := self.header[index];
    column := hdr;
    result := true;
  end;
end;

function TDBRows.GetColumnsCount: Integer;
begin
  result := self.header.Count;
end;

{ TDBRowColumn }

constructor TDBRowColumn.Create;
begin
  inherited Create;
  self.SetName('');
end;

destructor TDBRowColumn.Destroy;
begin
  inherited Destroy;
end;

constructor TDBRowColumn.Copy(sample: TStringMap);
begin
  self.CopyFrom(sample);
end;

procedure TDBRowColumn.CopyFrom(sample: TStringMap);
begin
  if assigned(sample) then begin
    if sample.exists('name') then self.SetName(sample.get('name'));
    if sample.exists('title') then self.SetTitle(sample.get('title'));
    if sample.exists('visible') then self.SetVisible(sample.get('visible'));
    if sample.exists('width') then self.SetVisible(sample.get('width'));
  end;
end;

function TDBRowColumn.getName: WideString;
begin
  if self.exists('name')
  then result := self.get('name')
  else result := '';
end;

procedure TDBRowColumn.setName(str: WideString);
begin
  self.put('name', str);
end;

function TDBRowColumn.GetTitle: WideString;
begin
  if self.exists('title')
  then result := self.get('title')
  else result := '';
end;

procedure TDBRowColumn.SetTitle(str: WideString);
begin
  self.put('title',str);
end;

function TDBRowColumn.GetVisible: boolean;
begin
  if self.exists('visible')
  then result := self.get('visible')
  else result := false;
end;

procedure TDBRowColumn.SetVisible(visible: boolean);
begin
  self.put('visible',visible);
end;

function TDBRowColumn.GetWidth: Integer;
begin
  if self.exists('width')
  then result := self.get('width')
  else result := 0;
end;

procedure TDBRowColumn.SetWidth(width: Integer);
begin
  self.put('width',width);
end;

initialization
log := logger('DBRows');

end.
