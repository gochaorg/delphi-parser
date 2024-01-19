unit WaybillForm;

// Содержит окно (модальное) для редактирования/добавления путевого листа

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls, ADODB, ComObj,

  Validation, DMLOperation,
  WaybillSQL, CarSQL, DispatcherSQL, DriverSQL,
  Loggers, Logging
  ;

type
  // Режим InsertMode / UpdateMode
  TMode = (InsertMode, UpdateMode);

  // Контроллер (окно) редактирования/добаления
  TWaybillController = class(TForm)
    outcomeDatetimeEdit: TLabeledEdit;
    incomeDatetimeEdit: TLabeledEdit;
    Panel1: TPanel;
    dispatcherGroupBox: TGroupBox;
    Splitter1: TSplitter;
    driverGroupBox: TGroupBox;
    dispatchersListBox: TListBox;
    driversListBox: TListBox;
    carGroupBox: TGroupBox;
    carEdit: TEdit;
    carsListBox: TListBox;
    carFindButton: TButton;
    wearEdit: TLabeledEdit;
    fuelConsEdit: TLabeledEdit;
    okButton: TButton;
    dispatcherPanel: TPanel;
    dispatcherFindButton: TButton;
    dispatcherEdit: TEdit;
    driverFindPanel: TPanel;
    driverFindButton: TButton;
    driverEdit: TEdit;
    errLabel: TLabel;

    // Завершение редактирования/добавления
    procedure okButtonClick(Sender: TObject);

    // Настройка/инициализация внутренних данных
    procedure FormShow(Sender: TObject);

    // Деконструкция внутренних данных
    procedure FormHide(Sender: TObject);

    // Поиск лиспетчера
    procedure dispatcherFindButtonClick(Sender: TObject);

    // Поиск водителя
    procedure driverFindButtonClick(Sender: TObject);

    // Поиск машины
    procedure carFindButtonClick(Sender: TObject);
  private
    // Режим InsertMode / UpdateMode
    mode: TMode;

    // содинение с СУБД
    // не владет ссылкой
    // сылка доступна только на момент пока окно открыто
    connection: TADOConnection;

    // ID добавленной записи в СУБД
    insertedId: Integer;

    // ID обновляемой записи
    updatingId: Integer;

    // добавление успешно выполнено
    insertSuccessfully: Boolean;

    // обновление успешно выполнено
    updateSuccessfully: Boolean;

    // Валидация и построение SQL
    waybillDataBuilder: TWaybillDataBuilder;

    // Поиск машин
    carFinder: ICarFinder;

    // Поиск диспетчеров
    dispatcherFinder: IDispatcherFinder;

    // Поиск водителей
    driverFinder: IDriverFinder;
  public
    // Открыть диалог для добавления
    // Аргументы
    //   connection - соединение с СУБД
    // Возвращает
    //   true - успешно добавлена запись
    //   false - не добавлена
    function InsertDialog(connection: TADOConnection): Boolean;

    // Возвращает id добавленной записи
    function GetInsertedId(): Integer;

    // Открыть диалог для обновления
    // Аргументы
    //   connection - соединение с СУБД
    //   id - идентификатор записи
    //   incomeDate  - дата возврата
    //   outcomeDate - дата выезда
    //   driverId - id водителя
    //   driverName - имя водителя
    //   dispatcherId - id диспетчера
    //   dispatcherName - имя диспетчера
    //   carId - id мащины
    //   carModelId - id модели машины
    //   carModelName - название модели машины
    //   carLegalNumber - гос номер машины
    //   carTotalWear - суммарный пробег
    //   wear - пробег для текущего путевого листа
    //   fuelCons - кол-во потребленного топлива
    // Возвращает
    //   true - успено обновлена запись
    //   false - ошибка
    function UpdateDialog(
      connection: TADOConnection;
      id: Integer;
      incomeDate:  WideString;
      outcomeDate: WideString;
      driverId: Integer;
      driverName: WideString;
      dispatcherId: Integer;
      dispatcherName: WideString;
      carId: Integer;
      carModelId: Integer;
      carModelName: WideString;
      carLegalNumber: WideString;
      carTotalWear: Integer;
      wear: Integer;
      fuelCons: Integer
    ): Boolean;

  private
    // Выполнения INSERT в СУБД
    procedure InsertData;

    // Выполнение UPDATE в СУБД
    procedure UpdateData;

    // Проверка введенных данных
    function Validate: boolean;
    procedure ValidateInput(Sender: TObject);

    // Добавление диспетчера в список на форму
    procedure AddDispatcher( dispatcher: TDispatcher );
    procedure ClearDispatchers();

    // Добавление водителя в список на форму
    procedure AddDriver( driver: TDriver );
    procedure ClearDrivers();

    // Добавление машины в список на форму
    procedure AddCar( car: TCar );
    procedure ClearCars();
  end;

var
  WaybillController: TWaybillController;

implementation

var
log: ILog;

{$R *.dfm}

function TWaybillController.GetInsertedId: Integer;
begin
  result := self.insertedId;
end;

function TWaybillController.InsertDialog(
  connection: TADOConnection
): Boolean;
begin
  result := false;
  self.connection := connection;
  self.carFinder := TCarFinder.Create(connection);
  self.driverFinder := TDriverFinder.Create(connection);
  self.dispatcherFinder := TDispatcherFinder.Create(connection);

  try
    self.mode := InsertMode;
    self.Caption := 'Добавить путевой лист';
    self.okButton.Caption := 'Добавить';
    Validate;
    ShowModal;
    result := insertSuccessfully;
  finally
    self.connection := nil;
  end;
end;

procedure TWaybillController.okButtonClick(Sender: TObject);
begin
  if validate then
  begin
    if mode = InsertMode
    then
      begin
        insertData();
      end
    else
      begin
        updateData();
      end;
  end;
end;

function TWaybillController.UpdateDialog(
  connection: TADOConnection;
  id: Integer;
  incomeDate:  WideString;
  outcomeDate: WideString;
  driverId: Integer;
  driverName: WideString;
  dispatcherId: Integer;
  dispatcherName: WideString;
  carId: Integer;
  carModelId: Integer;
  carModelName: WideString;
  carLegalNumber: WideString;
  carTotalWear: Integer;
  wear: Integer;
  fuelCons: Integer
): Boolean;
var
  fmt: TFormatSettings;
begin
  result := false;

  self.connection := connection;
  self.carFinder := TCarFinder.Create(connection);
  self.driverFinder := TDriverFinder.Create(connection);
  self.dispatcherFinder := TDispatcherFinder.Create(connection);

  try
    self.mode := UpdateMode;
    self.Caption := 'Обновить путевой лист';
    self.okButton.Caption := 'Обновить';

    fmt.ShortDateFormat :='yyyy-MM-dd';
    fmt.DateSeparator :='-';
    fmt.LongTimeFormat :='HH:nn:ss.zzz';
    fmt.TimeSeparator :=':';

    self.updatingId := id;
    self.outcomeDatetimeEdit.Text := outcomeDate;
    self.incomeDatetimeEdit.Text := incomeDate;

    self.ClearDrivers;
    self.AddDriver(
      TDriver.Create(driverId, driverName));
    self.driversListBox.ItemIndex := 0;

    self.ClearDispatchers;
    self.AddDispatcher(
      TDispatcher.Create(dispatcherId, dispatcherName));
    self.dispatchersListBox.ItemIndex := 0;

    self.ClearCars;
    self.AddCar(
      TCar.Create(
        carId,
        carLegalNumber,
        carModelId,
        carModelName,
        carTotalWear)
    );
    self.carsListBox.ItemIndex := 0;

    self.wearEdit.Text := IntToStr(wear);
    self.fuelConsEdit.Text := IntToStr(fuelCons);

    self.Validate;

    ShowModal;
    result := updateSuccessfully;
  finally
    self.connection := nil;
  end;
end;

procedure TWaybillController.FormShow(Sender: TObject);
begin
  if not assigned(self.waybillDataBuilder) then
    self.waybillDataBuilder := TWaybillDataBuilder.Create;

  carsListBox.OnClick := self.ValidateInput;
  driversListBox.OnClick := self.ValidateInput;
  dispatchersListBox.OnClick := self.ValidateInput;

  incomeDatetimeEdit.OnChange := self.ValidateInput;
  outcomeDatetimeEdit.OnChange := self.ValidateInput;
  wearEdit.OnChange := self.ValidateInput;
  fuelConsEdit.OnChange := self.ValidateInput;
end;

procedure TWaybillController.FormHide(Sender: TObject);
begin
  if assigned(self.waybillDataBuilder) then
    FreeAndNil(self.waybillDataBuilder);

  ClearDispatchers;
  ClearDrivers;
  ClearCars;  

  self.carFinder := nil;
  self.dispatcherFinder := nil;
  self.driverFinder := nil;
end;

procedure TWaybillController.InsertData;
begin
  if assigned(self.waybillDataBuilder) then begin
    try
      self.insertedId :=
        self.waybillDataBuilder.BuildInsert.Execute(self.connection);
      self.insertSuccessfully := true;
      Close;
    except
      on e:EWaybillDataBuilder do begin
        ShowMessage('Ошибка ввода данных '+e.Message);
      end;
      on e:EOleException do begin
        ShowMessage('Ошибка добавления данных '+e.Message);
      end;
    end;
  end;
end;

procedure TWaybillController.UpdateData;
begin
  if assigned(self.waybillDataBuilder) then begin
    try
      self.waybillDataBuilder.BuildUpdate.Execute(self.connection);
      self.updateSuccessfully := true;
      Close;
    except
      on e:EWaybillDataBuilder do begin
        ShowMessage('Ошибка ввода данных '+e.Message);
      end;
      on e:EOleException do begin
        ShowMessage('Ошибка обновления данных '+e.Message);
      end;
    end;
  end;
end;

function TWaybillController.Validate: boolean;
  procedure ok;
  begin
    errLabel.Caption := '';
    okButton.Enabled := true;
    validate := true;
  end;

  procedure err(messageText:WideString);
  begin
    errLabel.Caption := messageText;
    okButton.Enabled := false;
    validate := false;
  end;
var
  validation: IDataValidation;
begin
  ok;

  if assigned(self.waybillDataBuilder) then begin

    self.waybillDataBuilder.Reset;

    if self.mode = UpdateMode then
      self.waybillDataBuilder.setWaybillId(self.updatingId);

    if self.carsListBox.ItemIndex >= 0 then begin
      self.waybillDataBuilder.setCarId(
        (self.carsListBox.Items.Objects[
          self.carsListBox.ItemIndex
        ] as TCar).carId
      );
    end;

    if self.driversListBox.ItemIndex >= 0 then begin
      self.waybillDataBuilder.setDriverId(
        (self.driversListBox.Items.Objects[
          self.driversListBox.ItemIndex
        ] as TDriver).id
      );
    end;

    if self.dispatchersListBox.ItemIndex >= 0 then begin
      self.waybillDataBuilder.setDispatcherId(
        (self.dispatchersListBox.Items.Objects[
          self.dispatchersListBox.ItemIndex
        ] as TDispatcher).id
      );
    end;

    self.waybillDataBuilder.setOutcomeDate(
      self.outcomeDatetimeEdit.Text
    );

    self.waybillDataBuilder.setIncomeDate(
      self.incomeDatetimeEdit.Text
    );

    self.waybillDataBuilder.setWear(
      self.wearEdit.Text
    );

    self.waybillDataBuilder.setFuelConsumption(
      self.fuelConsEdit.Text
    );

    if self.mode = InsertMode then begin
      validation := self.waybillDataBuilder.validateInsert;
    end else begin
      validation := self.waybillDataBuilder.validateUpdate;
    end;

    if not validation.isOk then begin
      err( validation.getMessage );
    end;

  end;

end;

procedure TWaybillController.ValidateInput(Sender: TObject);
begin
  validate;
end;

procedure TWaybillController.dispatcherFindButtonClick(Sender: TObject);
begin
  if assigned(self.dispatcherFinder) then begin
    self.ClearDispatchers;
    if length(self.dispatcherEdit.Text)>0 then
      self.dispatcherFinder.findAll(self.AddDispatcher)
    else
      self.dispatcherFinder.findLike(self.dispatcherEdit.Text, self.AddDispatcher);
  end;
end;

procedure TWaybillController.AddDispatcher(dispatcher: TDispatcher);
begin
  self.dispatchersListBox.AddItem(
    dispatcher.name,
    TDispatcher.Copy(dispatcher)
  );
end;

procedure TWaybillController.ClearDispatchers;
var
  i:Integer;
  dispatcher: TDispatcher;
begin
  for i:=self.dispatchersListBox.Count-1 downto 0 do begin
    dispatcher := self.dispatchersListBox.Items.Objects[i] as TDispatcher;
    dispatcher.Destroy;
    self.dispatchersListBox.Items.Delete(i);
  end;
end;

//----------------

procedure TWaybillController.driverFindButtonClick(Sender: TObject);
begin
  if assigned(self.driverFinder) then begin
    self.ClearDrivers;
    if length(self.driverEdit.Text)>0 then
      self.driverFinder.findAll(self.AddDriver)
    else
      self.driverFinder.findLike(self.driverEdit.Text, self.AddDriver);
  end;
end;

procedure TWaybillController.ClearDrivers;
var
  i:Integer;
  driver: TDriver;
begin
  for i:=self.driversListBox.Count-1 downto 0 do begin
    driver := self.driversListBox.Items.Objects[i] as TDriver;
    driver.Destroy;
    self.driversListBox.Items.Delete(i);
  end;
end;

procedure TWaybillController.AddDriver(driver: TDriver);
begin
  self.driversListBox.AddItem(
    driver.name,
    TDriver.Copy(driver)
  );
end;

//----------------

procedure TWaybillController.carFindButtonClick(Sender: TObject);
begin
  if assigned(self.carFinder) then begin
    self.ClearCars;
    if length(self.carEdit.Text)>0 then
      self.carFinder.findAll(self.AddCar)
    else
      self.carFinder.findLike(self.carEdit.Text, self.AddCar);
  end;
end;

procedure TWaybillController.AddCar(car: TCar);
begin
  self.carsListBox.AddItem(
    'гос номер:'+car.legalNumber+
    ' модель:'+car.modelName+
    ' пробег:'+IntToStr(car.totalWear),
    TCar.Copy(car)
  );
end;

procedure TWaybillController.ClearCars;
var
  i:Integer;
  car: TCar;
begin
  for i:=self.carsListBox.Count-1 downto 0 do begin
    car := self.carsListBox.Items.Objects[i] as TCar;
    car.Destroy;
    self.carsListBox.Items.Delete(i);
  end;
end;

initialization
log := logger('WaybillForm');

end.
