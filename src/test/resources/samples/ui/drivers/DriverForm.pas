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
  // ����� InsertMode / UpdateMode
  TMode = (InsertMode, UpdateMode);

  // ����������/��������� ���� ��� ��������������/���������� 
  // ���������� � ��������
  TDriverController = class(TForm)
    nameEdit: TLabeledEdit;
    birthDayEdit: TLabeledEdit;
    okButton: TButton;
    errLabel: TLabel;

    // ������������� ����������
    procedure FormShow(Sender: TObject);

    // ������������� ����������
    procedure FormHide(Sender: TObject);

    // ���������� ��������������
    procedure okButtonClick(Sender: TObject);
  private
    // ����� InsertMode / UpdateMode
    mode: TMode;

    // ��������� � ����
    // �� ������ �������
    // ����� �������� ������ �� ������ ���� ���� �������
    connection: TADOConnection;

    // ID ����������� ������ � ����
    insertedId: Integer;

    // ID ����������� ������
    updatingId: Integer;

    // ���������� ������� ���������
    insertSuccessfully: Boolean;

    // ���������� ������� ���������
    updateSuccessfully: Boolean;

    // ��������� � ���������� SQL
    driverDataBuilder: TDriverDataBuilder;
  public
    // ������� ������ ��� ����������
    // ���������
    //   connection - ���������� � ����
    // ����������
    //   true - ������� ��������� ������
    //   false - �� ���������
    function InsertDialog(connection: TADOConnection): Boolean;

    // ���������� id ����������� ������
    function GetInsertedId(): Integer;

    // ������� ������ ��� ����������
    // ���������
    //   connection - ���������� � ����
    //   id - ������������� ������
    //   name - ���
    //   birthDate - ���� ��������
    // ����������
    //   true - ������ ��������� ������
    //   false - ������
    function UpdateDialog(
      connection: TADOConnection;
      id: Integer;
      name: WideString;
      birthDate: WideString
    ): Boolean;

  private
    // ��������� ������
    procedure InsertData;

    // �������������� ������
    procedure UpdateData;

    // ��������� ������
    // ���������
    //   true - �������
    //   false - ���� ������ ������� ������
    function Validate: boolean;

    // ��������� ������
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
    self.Caption := '���������� ��������';
    self.okButton.Caption := '��������';
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
        ShowMessage('�������� �� ���������� ������ '+e.Message);
      end;
      on e:EOleException do begin
        log.println('insert driver error: '+e.Message);
        ShowMessage('������ ���������� ������ '+e.Message);
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
    self.Caption := '���������� ��������';
    self.okButton.Caption := '��������';
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
        ShowMessage('�������� �� ���������� ������ '+e.Message);
      end;
      on e:EOleException do begin
        log.println('update error: '+e.Message);
        ShowMessage('������ ���������� ������ '+e.Message);
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
