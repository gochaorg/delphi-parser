unit DbConfForm;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls, DB, ADODB, Config, ComObj,

  Loggers, Logging, Menus;

type
  // ��������� ����������� � ����
  TDbConfController = class(TForm)
    passwordEdit: TEdit;
    passwordLabel: TLabel;
    userNameEdit: TLabeledEdit;
    connectionStringEdit: TLabeledEdit;
    testConnectionButton: TButton;
    applyButton: TButton;
    closeButton: TButton;
    ADOConnectionTest: TADOConnection;
    saveButton: TButton;
    connectionStringPopupMenu: TPopupMenu;
    setDefaultString: TMenuItem;

    // ������������ �����������
    procedure testConnectionButtonClick(Sender: TObject);

    // ���������� ��������
    procedure applyButtonClick(Sender: TObject);

    // ���������� ��������
    procedure saveButtonClick(Sender: TObject);

    // �������� ����
    procedure closeButtonClick(Sender: TObject);
    procedure setDefaultStringClick(Sender: TObject);
  private
     conf: IConfig;
     confSave: IConfigSave;
  public
    // �������������� �������� - ����������� � ����
    // ���������
    // conf - ������������
    procedure edit( conf: IConfig; confSave: IConfigSave );
  end;

var
  DbConfController: TDbConfController;

implementation

var
  log: ILog;

{$R *.dfm}

{ TDbConfController }

procedure TDbConfController.edit(conf: IConfig; confSave: IConfigSave);
begin
  self.conf     := conf;
  self.confSave := confSave;

  userNameEdit.Text         := conf.dbUsername;
  connectionStringEdit.Text := conf.dbConnectionString;
  passwordEdit.Text         := conf.dbPassword;

  ShowModal();

  self.conf     := nil;
  self.confSave := nil;
end;

procedure TDbConfController.testConnectionButtonClick(Sender: TObject);
var
  conf: TConfig;
begin
  ADOConnectionTest.ConnectionString := connectionStringEdit.Text;
  try
    ADOConnectionTest.Open(userNameEdit.Text, passwordEdit.Text);
    if ADOConnectionTest.Connected then ShowMessage('���������� �����������');
    ADOConnectionTest.Close();
  except
    on e: EOleException do begin
      ShowMessage('������ ����������:'+e.Message);
    end;
  end;
end;

procedure TDbConfController.applyButtonClick(Sender: TObject);
begin
  if assigned(conf) then begin
    conf.dbConnectionString := connectionStringEdit.Text;
    conf.dbUserName := userNameEdit.Text;
    conf.dbPassword := passwordEdit.Text;
  end;
end;

procedure TDbConfController.saveButtonClick(Sender: TObject);
begin
  if assigned(confSave) then begin
    try
      confSave.Save;
    except
      on e:EConfigSave do ShowMessage('������ ���������� '+e.Message);
    end;
  end;
end;

procedure TDbConfController.closeButtonClick(Sender: TObject);
begin
  Close();
end;

procedure TDbConfController.setDefaultStringClick(Sender: TObject);
begin
  self.connectionStringEdit.Text :=
    'SQLOLEDB.1;'+
    'Persist Security Info=False;'+
    'Initial Catalog=db_name;'+
    'Data Source=localhost';
end;

initialization
log := logger('DbConfForm');

end.
