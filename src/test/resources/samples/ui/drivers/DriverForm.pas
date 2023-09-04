unit DriverForm;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls,
  ADODB, ComObj, DB,


  DriverSQL, DMLOperation,
  Validation,
  Logging, Loggers;

type
  // Режим InsertMode / UpdateMode
  TMode = (InsertMode, UpdateMode);

  // Контроллер/модальное окно для редактирования/добавления 
  // информации о водителе
  TDriverController = class(TForm)
    nameEdit: TLabeledEdit;
    birthDayEdit: TLabeledEdit;
    okButton: TButton;
    errLabel: TLabel;

    // Инициализация переменных
    procedure FormShow(Sender: TObject);

    // Деконструкция переменных
    procedure FormHide(Sender: TObject);

    // Завершение редактирования
    procedure okButtonClick(Sender: TObject);
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
    driverDataBuilder: TDriverDataBuilder;
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
    //   name - Имя
    //   birthDate - Дата рождения
    // Возвращает
    //   true - успено обновлена запись
    //   false - ошибка
    function UpdateDialog(
      connection: TADOConnection;
      id: Integer;
      name: WideString;
      birthDate: WideString
    ): Boolean;

  private
    // Добавлене записи
    procedure InsertData;

    // Редактирование записи
    procedure UpdateData;

    // Валидация данных
    // Результат
    //   true - успешно
    //   false - есть ошибки входных данных
    function Validate: boolean;

    // Валидация данных
    procedure ValidateInput(Sender: TObject);
  end;

var
  DriverController: TDriverController;

implementation

var
log: ILog;

{$R *.dfm}

{ TDriverController }

procedure TDriverController.FormShow(Sender: TObject);
begin
  if not assigned(self.driverDataBuilder) then
    self.driverDataBuilder := TDriverDataBuilder.Create;

  birthDayEdit.OnChange := self.ValidateInput;
  nameEdit.OnChange := self.ValidateInput;
end;

procedure TDriverController.FormHide(Sender: TObject);
begin
  if assigned(self.driverDataBuilder) then
    FreeAndNil(self.driverDataBuilder);
end;

/////////////////////////////////////////////////

function TDriverController.GetInsertedId: Integer;
begin
  result := self.insertedId;
end;

function TDriverController.InsertDialog(
  connection: TADOConnection): Boolean;
begin
  log.println('InsertDialog');

  try
    self.connection := connection;
    self.mode := InsertMode;
    self.Caption := 'Добавление водителя';
    self.okButton.Caption := 'Добавить';
    self.insertSuccessfully := false;


    validateInput(self);
    self.ShowModal;
    result := insertSuccessfully;
    log.println('InsertDialog insertSuccessfully='+BoolToStr(result));
  finally
    self.connection := nil;
  end;
end;

procedure TDriverController.InsertData;
var
  dmlOp: IDMLOperation;
  id: Variant;
begin
  if assigned(self.driverDataBuilder) then begin
    try
      dmlOp := self.driverDataBuilder.buildInsert;
      id := dmlOp.Execute( self.connection );
      self.insertedId := id;
      self.insertSuccessfully := true;
      Close;
    except
      on e:EDriverDataBuilder do begin
        log.println('incorrect data input '+e.Message);
        ShowMessage('Введенны не корректные данные '+e.Message);
      end;
      on e:EOleException do begin
        log.println('insert driver error: '+e.Message);
        ShowMessage('Ощибка добавления записи '+e.Message);
      end;
    end;
  end;
end;

function TDriverController.UpdateDialog(
  connection: TADOConnection;
  id: Integer;
  name, birthDate: WideString
): Boolean;
begin
  log.println('UpdateDialog');
  log.println('  name='+name);
  log.println('  birthDate='+birthDate);
  log.println('  id='+IntToStr(id));

  try
    self.connection := connection;
    self.mode := UpdateMode;
    self.Caption := 'Обновление водителя';
    self.okButton.Caption := 'Обновить';
    self.updateSuccessfully := false;

    self.updatingId := id;
    self.nameEdit.Text := name;
    self.birthDayEdit.Text := birthDate;

    validateInput(self);
    self.ShowModal;

    result := updateSuccessfully;
    log.println('InsertDialog updateSuccessfully='+BoolToStr(result));
  finally
    self.connection := nil;
  end;
end;

procedure TDriverController.UpdateData;
var
  dmlOp: IDMLOperation;
  id: Variant;
begin
  if assigned(self.driverDataBuilder) then begin
    try
      dmlOp := self.driverDataBuilder.buildUpdate;
      id := dmlOp.Execute( self.connection );
      self.updateSuccessfully := true;
      Close;
    except
      on e:EDriverDataBuilder do begin
        log.println('incorrect data input '+e.Message);
        ShowMessage('Введенны не корректные данные '+e.Message);
      end;
      on e:EOleException do begin
        log.println('update error: '+e.Message);
        ShowMessage('Ощибка обновления данных '+e.Message);
      end;
    end;
  end;
end;


procedure TDriverController.okButtonClick(Sender: TObject);
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

function TDriverController.Validate: boolean;
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

  if assigned(self.driverDataBuilder) then begin
    self.driverDataBuilder.reset;

    if self.mode = UpdateMode then
      self.driverDataBuilder.setDriverID(self.updatingId);

    self.driverDataBuilder.setName(nameEdit.Text);
    self.driverDataBuilder.setBirthDay(birthDayEdit.Text);

    if self.mode = InsertMode then begin
      validation := self.driverDataBuilder.validateInsert;
    end else begin
      validation := self.driverDataBuilder.validateUpdate;
    end;

    if not validation.isOk then begin
      err( validation.getMessage );
    end;
  end;
end;

procedure TDriverController.ValidateInput(Sender: TObject);
begin
  validate;
end;

initialization
log := logger('DriverForm');

end.
