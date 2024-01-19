unit Config;

interface

uses SysUtils, Classes, IniFiles;

type
  // ������� ������ ��������
  TConfigReader = class
    procedure read( ini: TIniFile ); virtual;
  end;

  // ������������ ��
  IDbConfig = interface
    // ��� ������������ DB
    function getDbUsername: WideString;
    procedure setDbUsername( user_name: WideString );
    property dbUsername : WideString
      read getDbUsername
      write setDbUsername;

    // ������ ������������ DB
    function getDbPassword: WideString;
    procedure setDbPassword( password: WideString );
    property dbPassword : WideString
      read getDbPassword
      write setDbPassword;

    // ������ ����������� � DB
    function getDbConnectionString: WideString;
    procedure setDbConnectionString( str:WideString );
    property dbConnectionString : WideString
      read getDbConnectionString
      write setDbConnectionString;
  end;

  // ���������� ������������
  IConfigSave = interface
    // ���������� �������� � ���� config.ini � ������� ��������
    // ������
    //   � ������ ������ ���������� ���������� EConfigSave
    procedure Save();
  end;

  IConfig = interface(IDbConfig)
    // ���������� ���-�� ������ - ���� �������
    function getRefCount: Integer;
  end;

  // ��������� �� ��������� ��������
  TConfigListener = procedure () of object;
  TConfigListenerHolder = class
    private
      listenerValue : TConfigListener;
    public
      constructor Create(listenerValue : TConfigListener);
      property listener:TConfigListener read listenerValue;
  end;

  TConfig = class(TInterfacedObject, IConfig, IConfigSave)
  private
    // ��� ������������ DB
    dbUserNameValue : WideString;

    // ������ ������������ DB
    dbPasswordValue : WideString;

    // ������ ����������� � DB
    dbConnectionStringValue : WideString;

    // ������ �������
    dateFormatValue: WideString;

    // ������ ������� ��� SQL
    sqlDateFormatValue: WideString;

    // ������ ������������� �������
    // �� SQLDateParam
    dateToSqlMethodValue: WideString;

    // ����� �������
    debug: boolean;

    // ����������
    listeners: TList;

    // ����������
    readers: TList;
  public
    // �������� ������� �� ���������� �� ���������
    constructor Create();

    // ����������� �����������
    // ���������
    //   - sample - ������ ��� �����������
    constructor Copy( const sample: TConfig );

    // ���������� ��������
    // ���������
    //  - fileName - ��� �����
    // ������
    //   � ������ ������ ���������� ���������� EConfigSave
    procedure Save( const fileName: WideString ); overload;

    // ���������� �������� � ���� config.ini � ������� ��������
    // ������
    //   � ������ ������ ���������� ���������� EConfigSave
    procedure Save(); overload;

    // ������ ��������
    // ���������
    //  - fileName - ��� �����
    // ������
    //   � ������ ������ ���������� ���������� EConfigLoad
    procedure Load( const fileName: WideString ); overload;

    // ������ �������� �� ����� config.ini � ������� ��������
    // ���� ���� ����������� ������������ �������� �� ���������
    // ������
    //   � ������ ������ ���������� ���������� EConfigLoad
    procedure Load(); overload;

    // �������� dbUsername
    function getDbUsername: WideString;
    procedure setDbUsername( userName: WideString );

    // �������� dbPassword
    function getDbPassword: WideString;
    procedure setDbPassword( password: WideString );

    // �������� dbConnectionString
    function getDbConnectionString: WideString;
    procedure setDbConnectionString( str:WideString );

    // �������� dateFormat
    // ������ �������
    function getDateFormat: WideString;

    // �������� sqlDateFormat
    // ������ ������� ������������ � SQL ������������  
    function getSqlDateFormat: WideString;

    // ������ ������������� �������
    // �� SQLDateParam
    function getDateToSqlMethod: WideString;

    // ���-�� ������
    function getRefCount(): Integer;

    function isDebug(): Boolean;

    // ������������ ���������/���������
    function isLogEnabled: Boolean;

    // ��� ��� ����� ������������ �������� ��������
    function getLogFilename: WideString;

    // ��������� � ��� ���� ������ ��� ������������ ��� ����
    function isAppendLogFile: Boolean;

    // ��������� ���������� �� ���������
    procedure addListener( listener: TConfigListener );

    // ��������� ���������� �� ������ ini �����
    procedure addReader( listener: TConfigReader );
  end;

  // ������ ���������� �������
  EConfigSave = class(Exception);

  // ������ ������ �������
  EConfigLoad = class(Exception);

const
  DEFAULT_DB_USERNAME = 'username';
  DEFAULT_DB_CONNECTION_STRING = 'Provider=SQLOLEDB.1;'+
    'Persist Security Info=False;'+
    'User ID=username;'+
    'Initial Catalog=db_name;Data Source=host';
  DEFAULT_DB_PASSWORD = 'password';
  DEFAULT_CONFIG_FILENAME = 'config.init';

// ���������� �������
var
  applicationConfigObj : TConfig;
  applicationConfigItf : IConfig;
  applicationConfigSaveItf : IConfigSave;

implementation

uses
   Dialogs;

const
  DB_SECTION = 'db';
  DB_USERNAME_KEY = 'user-name';
  DB_PASSWORD_KEY = 'password';
  DB_CONNECTION_STRING_KEY = 'connection-string';

{ TConfig }

constructor TConfig.Copy(const sample: TConfig);
begin
  Inherited Create();
  self.dbConnectionStringValue := sample.dbConnectionStringValue;
  self.dbUserNameValue := sample.dbUserNameValue;
  self.dbPasswordValue := sample.dbPasswordValue;

  self.debug := sample.debug;

  self.listeners := TList.Create;
end;

constructor TConfig.Create;
begin
  Inherited Create();
  self.dbConnectionStringValue := DEFAULT_DB_CONNECTION_STRING;
  self.dbUserNameValue := DEFAULT_DB_USERNAME;
  self.dbPasswordValue := DEFAULT_DB_PASSWORD;

  self.debug := true;

  self.listeners := TList.Create;
  self.readers := TList.Create;
end;

procedure TConfig.Load(const fileName: WideString);
var
  iniFile: TIniFile;
  i:Integer;
  ls: TConfigListenerHolder;
  reader: TConfigReader;
begin
  try
    try
      iniFile := TIniFile.Create(fileName);
      self.dbConnectionStringValue := iniFile.ReadString(DB_SECTION, DB_CONNECTION_STRING_KEY, DEFAULT_DB_CONNECTION_STRING);
      self.dbUserNameValue := iniFile.ReadString(DB_SECTION, DB_USERNAME_KEY, DEFAULT_DB_USERNAME);
      self.dbPasswordValue := iniFile.ReadString(DB_SECTION, DB_PASSWORD_KEY, DEFAULT_DB_PASSWORD);

      debug := iniFile.ReadBool('debug', 'default', true);

      self.sqlDateFormatValue := iniFile.ReadString(
        DB_SECTION,
        'date-format',
        ''
      );

      self.dateToSqlMethodValue := iniFile.ReadString(
        DB_SECTION,
        'date-to-sql',
        ''
      );

      self.dateFormatValue := iniFile.ReadString(
        'date-format',
        'format',
        ''
      );

      ///
      for i:=0 to (self.readers.Count-1) do begin
        reader := self.readers[i];
        if assigned(reader) then begin
          reader.read(iniFile);
        end;
      end;

      ///
      for i:=0 to (self.listeners.Count-1) do begin
        ls := self.listeners[i];
        ls.listener();
      end;

    except
      on e: EIniFileException do raise EConfigLoad.Create(e.Message);
    end;
  finally
    FreeAndNil( iniFile );
  end;
end;

procedure TConfig.Save(const fileName: WideString);
var
  iniFile: TIniFile;
begin
  try
    try
      iniFile := TIniFile.Create(fileName);
      iniFile.WriteString(DB_SECTION, DB_CONNECTION_STRING_KEY, dbConnectionStringValue);
      iniFile.WriteString(DB_SECTION, DB_USERNAME_KEY, dbUserNameValue);
      iniFile.WriteString(DB_SECTION, DB_PASSWORD_KEY, dbPasswordValue);

      iniFile.WriteBool('debug', 'default', debug);
    except
      on e: EIniFileException do raise EConfigSave.Create(e.Message);
    end;
  finally
    FreeAndNil( iniFile );
  end;
end;

procedure TConfig.Save;
begin
  Save( GetCurrentDir() + '\\' + 'config.ini' );
end;

procedure TConfig.Load;
begin
  Load( GetCurrentDir() + '\\' + 'config.ini' );
end;

// �������� dbConnectionString

function TConfig.GetDbConnectionString: WideString;
begin
  result := self.dbConnectionStringValue;
end;

procedure TConfig.SetDbConnectionString(str: WideString);
begin
  self.dbConnectionStringValue := str;
end;

// �������� dbPassword

function TConfig.GetDbPassword: WideString;
begin
  result := self.dbPasswordValue;
end;

procedure TConfig.SetDbPassword(password: WideString);
begin
  self.dbPasswordValue := password;
end;

// �������� DbUsername

function TConfig.GetDbUsername: WideString;
begin
  result := self.dbUserNameValue;
end;

procedure TConfig.SetDbUsername(userName: WideString);
begin
  self.dbUserNameValue := userName;
end;

function TConfig.GetRefCount: Integer;
begin
  result := RefCount;
end;

function TConfig.IsDebug: Boolean;
begin
  result := self.debug;
end;

procedure TConfig.addListener(listener: TConfigListener);
begin
  if assigned(listener) then
    self.listeners.Add(TConfigListenerHolder.Create(listener));
end;

function TConfig.getLogFilename: WideString;
begin
  result := 'app.log';
end;

function TConfig.isLogEnabled: Boolean;
begin
  result := true;
end;

function TConfig.isAppendLogFile: Boolean;
begin
  result := false;
end;

procedure TConfig.addReader(listener: TConfigReader);
begin
  self.readers.Add(listener);
end;

function TConfig.getDateFormat: WideString;
begin
  if length(self.dateFormatValue)>0 then
    result := self.dateFormatValue
  else
    result := '%Y-%M-%D';
end;

function TConfig.getSqlDateFormat: WideString;
begin
  if length(self.sqlDateFormatValue)>0 then
    result := self.sqlDateFormatValue
  else
    result := '%Y-%M-%D 00:00:00.000';
end;

function TConfig.getDateToSqlMethod: WideString;
begin
  result := self.dateToSqlMethodValue;
end;

{ TConfigListenerHolder }

constructor TConfigListenerHolder.Create(listenerValue: TConfigListener);
begin
  self.listenerValue := listenerValue;
end;

{ TConfigReader }

procedure TConfigReader.read(ini: TIniFile);
begin
 //
end;

initialization
applicationConfigObj := TConfig.Create();

applicationConfigItf := applicationConfigObj;
applicationConfigSaveItf := applicationConfigObj;

end.
 