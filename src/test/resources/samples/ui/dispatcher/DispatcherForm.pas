unit DispatcherForm;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls,
  ADODB, ComObj, DB,

  DispatcherSQL, DMLOperation,
  Validation,
  Logging, Loggers;

type
  // ����� InsertMode / UpdateMode
  TMode = (InsertMode, UpdateMode);

  TDispatcherController = class(TForm)
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
    dispatcherDataBuilder: TDispatcherDataBuilder;
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
  DispatcherController: TDispatcherController;

implementation

var
  log: ILog;

{$R *.dfm}

procedure TDispatcherController.FormShow(Sender: TObject);
begin
  if not assigned(self.dispatcherDataBuilder) then
    self.dispatcherDataBuilder := TDispatcherDataBuilder.Create;

  birthDayEdit.OnChange := self.ValidateInput;
  nameEdit.OnChange := self.ValidateInput;
end;

procedure TDispatcherController.FormHide(Sender: TObject);
begin
  if assigned(self.dispatcherDataBuilder) then
    FreeAndNil(self.dispatcherDataBuilder);
end;

function TDispatcherController.GetInsertedId: Integer;
begin
  result := self.insertedId;
end;

function TDispatcherController.InsertDialog(
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

procedure TDispatcherController.InsertData;
var
  dmlOp: IDMLOperation;
  id: Variant;
begin
  if assigned(self.dispatcherDataBuilder) then begin
    try
      dmlOp := self.dispatcherDataBuilder.buildInsert;
      id := dmlOp.Execute( self.connection );
      self.insertedId := id;
      self.insertSuccessfully := true;
      Close;
    except
      on e:EDispatcherDataBuilder do begin
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

function TDispatcherController.UpdateDialog(
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

procedure TDispatcherController.UpdateData;
var
  dmlOp: IDMLOperation;
  id: Variant;
begin
  if assigned(self.dispatcherDataBuilder) then begin
    try
      dmlOp := self.dispatcherDataBuilder.buildUpdate;
      id := dmlOp.Execute( self.connection );
      self.updateSuccessfully := true;
      Close;
    except
      on e:EDispatcherDataBuilder do begin
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

procedure TDispatcherController.okButtonClick(Sender: TObject);
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

function TDispatcherController.Validate: boolean;
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

  if assigned(self.dispatcherDataBuilder) then begin
    self.dispatcherDataBuilder.reset;

    if self.mode = UpdateMode then
      self.dispatcherDataBuilder.setDriverID(self.updatingId);

    self.dispatcherDataBuilder.setName(nameEdit.Text);
    self.dispatcherDataBuilder.setBirthDay(birthDayEdit.Text);

    if self.mode = InsertMode then begin
      validation := self.dispatcherDataBuilder.validateInsert;
    end else begin
      validation := self.dispatcherDataBuilder.validateUpdate;
    end;

    if not validation.isOk then begin
      err( validation.getMessage );
    end;
  end;
end;

procedure TDispatcherController.ValidateInput(Sender: TObject);
begin
  validate;
end;

initialization
log := logger('DispatcherForm');

end.
