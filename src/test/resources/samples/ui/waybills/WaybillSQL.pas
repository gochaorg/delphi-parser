unit WaybillSQL;

interface

uses
  SysUtils, ADODB, 

  SQLDateParam,
  Logging, Loggers,
  MyDate,
  Map,
  DMLOperation,
  Validation;

type

// ������ ��������/��������� ��� ���������� ��������
EWaybillDataBuilder = class(Exception);

// ������ ���������
// ��� �������� ���� ������� insert ���� update
IWaybillDataBuilder = interface
  // ����� ���������, ���� ������ ������� ��������
  procedure Reset;

  // �������� ������ ����� INSERT
  function ValidateInsert: IDataValidation;

  // ������� �������� INSERT.
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� EDriverDataBuilder
  function BuildInsert: IDMLOperation;

  // �������� ������ ����� UPDATE
  function ValidateUpdate: IDataValidation;

  // ������� �������� UPDATE.
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� EDriverDataBuilder
  function BuildUpdate: IDMLOperation;

  // �������� id �������� �����
  procedure setWaybillId( id:Integer );

  // ��������� ����/����� ������
  procedure setOutcomeDate( date:TDateTime ); overload;
  // ��������� ����/����� ������
  // ���������
  //   str - ������ � ������� yyyy-MM-dd hh:mm:ss
  procedure setOutcomeDate( str:WideString ); overload;

  // ��������� ����/����� �������
  procedure setIncomeDate( date:TDateTime ); overload;
  // ��������� ����/����� �������
  // ���������
  //   str - ������ � ������� yyyy-MM-dd hh:mm:ss
  procedure setIncomeDate( str:WideString ); overload;

  // ��������� id ��������
  procedure setDriverId( driverId:Integer );

  // ��������� id ����������
  procedure setDispatcherId( dispatcherId:Integer );

  // ��������� id ������
  procedure setCarId( carId:Integer );

  // ��������� ������
  procedure setWear( wear:Integer ); overload;

  // ��������� ������
  // ���������
  //   wear - ������ ���������� �����
  procedure setWear( wear:WideString ); overload;

  // ��������� ������ �������
  procedure setFuelConsumption( liters:Integer ); overload;
  // ��������� ������ �������
  // ���������
  //   liters - ������ ���������� �����
  procedure setFuelConsumption( liters:WideString ); overload;
end;

TWaybillDataBuilder = class(TInterfacedObject, IWaybillDataBuilder)
  private
    waybillId:Integer;
    waybillIdExists:boolean;

    outcomeDate:TDateTime;
    outcomeDateExists:boolean;
    outcomeDateConvError:WideString;

    incomeDate:TDateTime;
    incomeDateExists:boolean;
    incomeDateConvError:WideString;

    carId:Integer;
    carIdExists:boolean;

    driverId:Integer;
    driverIdExists:boolean;

    dispatcherId:Integer;
    dispatcherIdExists:boolean;

    wear:Integer;
    wearExists:boolean;
    wearConvError:WideString;

    fuelConsumption:Integer;
    fuelConsumptionExists:boolean;
    fuelConvError:WideString;
  public
    constructor Create;
    destructor Destroy; override;

    procedure Reset;

    function ValidateInsert: IDataValidation;
    function BuildInsert: IDMLOperation;

    function ValidateUpdate: IDataValidation;
    function BuildUpdate: IDMLOperation;

    procedure setWaybillId( id:Integer );

    procedure setOutcomeDate( date:TDateTime ); overload;
    procedure setOutcomeDate( str:WideString ); overload;

    procedure setIncomeDate( date:TDateTime ); overload;
    procedure setIncomeDate( str:WideString ); overload;

    procedure setDriverId( driverId:Integer );

    procedure setDispatcherId( dispatcherId:Integer );

    procedure setCarId( carId:Integer );

    procedure setWear( wear:Integer ); overload;
    procedure setWear( wear:WideString ); overload;

    procedure setFuelConsumption( liters:Integer ); overload;
    procedure setFuelConsumption( liters:WideString ); overload;
  private
    function Validate(insert:boolean): IDataValidation;
end;

implementation

var
log: ILog;

constructor TWaybillDataBuilder.Create;
begin
  inherited Create;
end;

destructor TWaybillDataBuilder.Destroy;
begin
  inherited Destroy;
end;

procedure TWaybillDataBuilder.Reset;
begin
  self.carIdExists := false;
  self.dispatcherIdExists := false;
  self.driverIdExists := false;
  self.fuelConsumptionExists := false;
  self.fuelConvError := '';
  self.incomeDateExists := false;
  self.incomeDateConvError := '';
  self.outcomeDateExists := false;
  self.outcomeDateConvError := '';
  self.waybillIdExists := false;
  self.wearExists := false;
  self.wearConvError := '';
end;

function TWaybillDataBuilder.Validate(insert:boolean): IDataValidation;
var
  validation : TDataValidation;
begin
  validation := TDataValidation.Create;

  if not insert then
    if not self.waybillIdExists then
      validation.addError('�� ������ ID');

  if not self.carIdExists then
    validation.addError('�� ������ ID ������');

  if not self.driverIdExists then
    validation.addError('�� ������ ID ��������');

  if not self.dispatcherIdExists then
    validation.addError('�� ������ ID ����������');

  if not self.outcomeDateExists then
    if length(self.outcomeDateConvError)>0
    then validation.addError(self.outcomeDateConvError)
    else validation.addError('�� ������� ���� ������');

  if not self.incomeDateExists then
    if length(self.incomeDateConvError)>0
    then validation.addError(self.incomeDateConvError)
    else validation.addError('�� ������� ���� ��������');

  if not self.wearExists then
    if length(self.wearConvError)>0
    then validation.addError(self.wearConvError)
    else validation.addError('�� ������ ������');

  if not self.fuelConsumptionExists then
    if length(self.fuelConvError)>0
    then validation.addError(self.fuelConvError)
    else validation.addError('�� ������ ������ �������');

  result := validation;
end;

function TWaybillDataBuilder.ValidateInsert: IDataValidation;
begin
  result := Validate(true);
end;

function TWaybillDataBuilder.BuildInsert: IDMLOperation;
var
  validation: IDataValidation;
  params: TStringMap;
  sql: String;
  fmt: TFormatSettings;
  date_str: String;
begin
  validation := validateInsert;
  if not validation.isOk then
    raise EWaybillDataBuilder.Create(validation.getMessage);

  fmt.ShortDateFormat :='yyyy-MM-dd';
  fmt.DateSeparator :='-';
  fmt.LongTimeFormat :='HH:nn:ss.zzz';
  fmt.TimeSeparator :=':';

  sql :=
    'insert into waybills ('+
    ' driver, dispatcher, car, outcome_date, income_date, '+
    ' wear, fuel_cons ) '+
    'values ( '+
    ' :driver, :dispatcher, :car, :outcome_date, :income_date, '+
    ' :wear, :fuel_cons ); ' +
    'select @@IDENTITY as _id';

  params := TStringMap.Create;
  params.put('driver',       self.driverId);
  params.put('dispatcher',   self.dispatcherId);
  params.put('car',          self.carId);

  date_str := DateTimeToStr(self.outcomeDate, fmt);
  params.put('outcome_date', date_str);

  date_str := DateTimeToStr(self.incomeDate, fmt);
  params.put('income_date',  date_str);

  params.put('wear',         self.wear);
  params.put('fuel_cons',    self.fuelConsumption);

  result := TSqlInsertOperation.Create( sql, params, '_id');
end;

function TWaybillDataBuilder.ValidateUpdate: IDataValidation;
begin
  result := Validate(false);
end;

function TWaybillDataBuilder.BuildUpdate: IDMLOperation;
var
  validation: IDataValidation;
  params: TStringMap;
  sql: String;
  fmt: TFormatSettings;
  date_str: String;
begin
  validation := validateUpdate;
  if not validation.isOk then
    raise EWaybillDataBuilder.Create(validation.getMessage);

  sql :=
    'update waybills set '+
    ' driver = :driver, dispatcher = :dispatcher, car = :car, '+
    ' outcome_date = :outcome_date, income_date = :income_date, '+
    ' wear = :wear, fuel_cons = :fuel_cons '+
    ' where '+
    ' id = :id';

  fmt.ShortDateFormat :='yyyy-MM-dd';
  fmt.DateSeparator :='-';
  fmt.LongTimeFormat :='HH:nn:ss.zzz';
  fmt.TimeSeparator :=':';

  params := TStringMap.Create;
  params.put('id',           self.waybillId);
  params.put('driver',       self.driverId);
  params.put('dispatcher',   self.dispatcherId);
  params.put('car',          self.carId);

  date_str := DateTimeToStr(self.outcomeDate, fmt);
  params.put('outcome_date', date_str);

  date_str := DateTimeToStr(self.incomeDate, fmt);
  params.put('income_date',  date_str);
  
  params.put('wear',         self.wear);
  params.put('fuel_cons',    self.fuelConsumption);

  result := TSqlUpdateOperation.Create( sql, params );
end;

procedure TWaybillDataBuilder.setCarId(carId: Integer);
begin
  self.carId := carId;
  self.carIdExists := true;
end;

procedure TWaybillDataBuilder.setDispatcherId(dispatcherId: Integer);
begin
  self.dispatcherId := dispatcherId;
  self.dispatcherIdExists := true;
end;

procedure TWaybillDataBuilder.setDriverId(driverId: Integer);
begin
  self.driverId := driverId;
  self.driverIdExists := true;
end;

procedure TWaybillDataBuilder.setFuelConsumption(liters: WideString);
begin
  if TryStrToInt(liters, self.fuelConsumption) then begin
    self.fuelConsumptionExists := true;
    self.fuelConvError := '';
  end else begin
    self.fuelConsumptionExists := false;
    self.fuelConvError := '������ ������� �� ������� ������';
  end;
end;

procedure TWaybillDataBuilder.setFuelConsumption(liters: Integer);
begin
  self.fuelConsumption := liters;
  self.fuelConsumptionExists := true;
  self.fuelConvError := '';
end;

procedure TWaybillDataBuilder.setIncomeDate(date: TDateTime);
begin
  self.incomeDate := date;
  self.incomeDateExists := true;
  self.incomeDateConvError := '';
end;

procedure TWaybillDataBuilder.setIncomeDate(str: WideString);
var
  datetime: TDateTime;
  fmt: TFormatSettings;
begin
  fmt.ShortDateFormat :='yyyy-MM-dd';
  fmt.DateSeparator :='-';
  fmt.LongTimeFormat :='HH:nn:ss';
  fmt.TimeSeparator :=':';

  if TryStrToDatetime( str, datetime, fmt ) then begin
    self.incomeDate := datetime;
    self.incomeDateExists := true;
    self.incomeDateConvError := '';
  end else begin
    self.incomeDateExists := false;
    self.incomeDateConvError := '�� ��������� ������ �������';
  end;
end;

procedure TWaybillDataBuilder.setOutcomeDate(str: WideString);
var
  datetime: TDateTime;
  fmt: TFormatSettings;
begin
  fmt.ShortDateFormat :='yyyy-MM-dd';
  fmt.DateSeparator :='-';
  fmt.LongTimeFormat :='HH:nn:ss';
  fmt.TimeSeparator :=':';

  if TryStrToDatetime( str, datetime, fmt ) then begin
    self.outcomeDate := datetime;
    self.outcomeDateExists := true;
    self.outcomeDateConvError := '';
  end else begin
    self.outcomeDateExists := false;
    self.outcomeDateConvError := '�� ��������� ������ �������';
  end;
end;

procedure TWaybillDataBuilder.setOutcomeDate(date: TDateTime);
begin
  self.outcomeDate := date;
  self.outcomeDateExists := true;
  self.outcomeDateConvError := '';
end;

procedure TWaybillDataBuilder.setWaybillId(id: Integer);
begin
  self.waybillId := id;
  Self.waybillIdExists := true;
end;

procedure TWaybillDataBuilder.setWear(wear: Integer);
begin
  self.wear := wear;
  self.wearExists := true;
  self.wearConvError := '';
end;

procedure TWaybillDataBuilder.setWear(wear: WideString);
begin
  if TryStrToInt(wear, self.wear) then begin
    self.wearExists := true;
    self.wearConvError := '';
  end else begin
    self.wearExists := false;
    self.wearConvError := '������ �� �������� ������';
  end;
end;

initialization
log := logger('WaybillSQL');

end.
