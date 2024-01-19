unit CarForm;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls,

  ADODB, ComObj, DB,
  Logging, Loggers,
  MyDate, CarSql, DMLOperation, Validation;

type
  // ����� InsertMode / UpdateMode
  TMode = (InsertMode, UpdateMode);

  // ������ ����������/���������� ������
  TCarController = class(TForm)
    legalNumberEdit: TLabeledEdit;
    modelListBox: TListBox;
    leagalNumLabel: TLabel;
    wearEdit: TLabeledEdit;
    birthYearEdit: TLabeledEdit;
    maintainceEdit: TLabeledEdit;
    okButton: TButton;
    insertADOQuery: TADOQuery;
    updateADOQuery: TADOQuery;
    carsModelADOQuery: TADOQuery;
    errLabel: TLabel;

    // ���������� ��������������
    procedure okButtonClick(Sender: TObject);

    // ������������� ����������
    procedure FormDestroy(Sender: TObject);

    // ������������� ����������
    procedure FormShow(Sender: TObject);
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

    sourceModelId: Integer;

    // ���������� ������� ���������
    insertSuccessfully: Boolean;

    // ���������� ������� ���������
    updateSuccessfully: Boolean;

    // ��������� � ���������� SQL
    carDataBuilder: TCarDataBuilder;
  private
    // �������� ������ �������
    procedure ClearCarsModelListbox();

    // ��������� ������
    procedure InsertData();

    // �������������� ������
    procedure UpdateData();

    procedure SetConnection( connection: TADOConnection );

    // ���������� ������ ������
    procedure RefreshModelList();

    // ��������� ������
    procedure ValidateInput(Sender: TObject);

    // ��������� ������
    // ���������
    //   true - �������
    //   false - ���� ������ ������� ������
    function Validate():boolean;
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
    //   legalNumber - ��� �����
    //   modelId - ������ �� ������ ������
    //   wear - ������
    //   birthYear - ��� �������
    //   maintenance - ���� ����������� ��
    // ����������
    //   true - ������ ��������� ������
    //   false - ������
    function UpdateDialog(
      connection: TADOConnection;
      id: Integer;
      legalNumber: WideString;
      modelId: Integer;
      wear: Integer;
      birthYear: Integer;
      maintenance: WideString
    ): Boolean;
  end;

var
  CarController: TCarController;

implementation

var
  log: ILog;

type
  TCarModelInfo = class(TObject)
    public
      name: variant;
      id: variant;
  end;

{$R *.dfm}

{ TCarController }

procedure TCarController.FormDestroy(Sender: TObject);
begin
  clearCarsModelListbox;
end;

function TCarController.GetInsertedId: Integer;
begin
  result := self.insertedId;
end;

procedure TCarController.InsertData;
var
  dmlOp: IDMLOperation;
  id: Variant;
begin
  if assigned(self.carDataBuilder) then begin
    try
      dmlOp := self.carDataBuilder.buildInsert;
      id := dmlOp.Execute( self.connection );
      self.insertedId := id;
      self.insertSuccessfully := true;
      Close;
    except
      on e:ECarDataBuilder do begin
        log.println('incorrect data input '+e.Message);
        ShowMessage('�������� �� ���������� ������ '+e.Message);
      end;
      on e:EOleException do begin
        log.println('insert car error: '+e.Message);
        ShowMessage('������ ���������� ������ '+e.Message);
      end;
    end;
  end;
end;

function TCarController.InsertDialog(connection: TADOConnection): Boolean;
begin
  log.println('insertDialog open');

  self.carDataBuilder := TCarDataBuilder.Create;
  self.setConnection(connection);
  self.mode := InsertMode;
  self.Caption := '���������� ������';
  self.okButton.Caption := '��������';
  self.insertSuccessfully := false;
  self.sourceModelId := -1;
  try
    refreshModelList;
    validateInput(self);
    ShowModal;
    result := insertSuccessfully;
    log.println('insertDialog closed, successfully='+boolToStr(result));
  finally
    FreeAndNil(self.carDataBuilder);
    self.setConnection(nil);
  end;
end;

procedure TCarController.UpdateData;
var
  dmlOp: IDMLOperation;
  id: Variant;
begin
  if assigned(self.carDataBuilder) then begin
    try
      dmlOp := self.carDataBuilder.buildUpdate;
      id := dmlOp.Execute( self.connection );
      self.updateSuccessfully := true;
      Close;
    except
      on e:ECarDataBuilder do begin
        log.println('incorrect data input '+e.Message);
        ShowMessage('�������� �� ���������� ������ '+e.Message);
      end;
      on e:EOleException do begin
        log.println('update car error: '+e.Message);
        ShowMessage('������ ���������� ������ ������ '+e.Message);
      end;
    end;
  end;
end;

function TCarController.UpdateDialog(
  connection: TADOConnection;
  id: Integer;
  legalNumber: WideString;
  modelId: Integer;
  wear: Integer;
  birthYear: Integer;
  maintenance: WideString
): Boolean;
begin
  log.println('updateDialog open');
  log.println('  carId='+IntToStr(id));
  log.println('  legalNumber='+legalNumber);
  log.println('  modelId='+intToStr(modelId));
  log.println('  wear='+IntToStr(wear));
  log.println('  birthYear='+IntToStr(birthYear));
  log.println('  maintenance='+maintenance);

  self.carDataBuilder := TCarDataBuilder.Create;
  self.setConnection(connection);
  self.mode := UpdateMode;

  self.updatingId := id;
  self.legalNumberEdit.Text := legalNumber;
  self.sourceModelId := modelId;
  self.wearEdit.Text := IntToStr(wear);
  self.birthYearEdit.Text := IntToStr(birthYear);
  self.maintainceEdit.Text := maintenance;

  self.Caption := '���������� ������';
  self.okButton.Caption := '��������';
  self.updateSuccessfully := false;
  try
    refreshModelList;
    ShowModal;
    result := updateSuccessfully;
    log.println('updateDialog closed, successfully='+boolToStr(result));
  finally
    FreeAndNil(self.carDataBuilder);
    self.setConnection(nil);
  end;
end;

procedure TCarController.okButtonClick(Sender: TObject);
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

procedure TCarController.RefreshModelList;
var
  model: TCarModelInfo;
  index: Integer;
  selectIndex: Integer;

begin
  selectIndex:=-1;
  clearCarsModelListbox;
  try
    try
      log.println('refreshModelList');

      self.carsModelADOQuery.Open;
      self.carsModelADOQuery.First;
      index := -1;
      while not self.carsModelADOQuery.Eof do begin
        index := index + 1;

        model := TCarModelInfo.Create;

        model.name :=
          self.carsModelADOQuery.FieldByName('name').Value;

        model.id :=
          self.carsModelADOQuery.FieldByName('id').Value;

        self.modelListBox.Items.AddObject(
          VarToStr(model.name),
          model
        );

        if self.carsModelADOQuery.FieldByName('id').Value =
          self.sourceModelId then selectIndex := index;

        self.carsModelADOQuery.Next;
      end;

      if selectIndex >=0 then
        self.modelListBox.ItemIndex := selectIndex;

    except
      on e:EOleException do begin
        log.println('got error: '+e.Message);
        ShowMessage('������ ��� ���������� ������ ������� '+e.Message);
      end;
    end;
  finally
    self.carsModelADOQuery.Close;
  end;
end;

procedure TCarController.SetConnection(connection: TADOConnection);
begin
  self.connection := connection;
  self.insertADOQuery.Connection := connection;
  self.updateADOQuery.Connection := connection;
  self.carsModelADOQuery.Connection := connection;
end;

procedure TCarController.ClearCarsModelListbox;
var
  i : Integer;
begin
  for i:=0 to self.modelListBox.Items.Count-1 do begin
    self.modelListBox.Items.Objects[i].Destroy;
  end;
  self.modelListBox.Items.Clear;
end;

procedure TCarController.ValidateInput(Sender: TObject);
begin
  validate;
end;

function TCarController.Validate: boolean;
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
  modelInfo: TCarModelInfo;
begin
  ok;

  if assigned(self.carDataBuilder) then begin
    self.carDataBuilder.reset;

    if self.mode = UpdateMode then
      self.carDataBuilder.setCarID(self.updatingId);

    self.carDataBuilder.setLegalNumber(legalNumberEdit.Text);

    if self.modelListBox.ItemIndex>=0 then begin
      modelInfo := self.modelListBox.Items.Objects[ self.modelListBox.ItemIndex ]
        as TCarModelInfo;
      self.carDataBuilder.setModelId(modelInfo.id);
    end;

    self.carDataBuilder.setWear(wearEdit.Text);
    self.carDataBuilder.setBirthYear(birthYearEdit.Text);
    self.carDataBuilder.setMaintainceDate(maintainceEdit.Text);

    if self.mode = InsertMode then begin
      validation := self.carDataBuilder.validateInsert;
    end else begin
      validation := self.carDataBuilder.validateUpdate;
    end;

    if not validation.isOk then begin
      err( validation.getMessage );
    end;
  end;
end;

procedure TCarController.FormShow(Sender: TObject);
begin
  legalNumberEdit.OnChange := self.validateInput;
  modelListBox.OnClick := self.validateInput;
  modelListBox.OnEnter := self.validateInput;
  wearEdit.OnChange := self.validateInput;
  birthYearEdit.OnChange := self.validateInput;
  maintainceEdit.OnChange := self.validateInput;
end;

initialization
  log := logger('CarForm');

end.
