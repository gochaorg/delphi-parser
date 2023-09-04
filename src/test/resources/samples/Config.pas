unit Config;

interface

uses SysUtils, Classes, IniFiles;

type
  // функция чтения настроек
  TConfigReader = class
    procedure read( ini: TIniFile ); virtual;
  end;

  // Конфигурация БД
  IDbConfig = interface
    // Имя пользователя DB
    function getDbUsername: WideString;
    procedure setDbUsername( user_name: WideString );
    property dbUsername : WideString
      read getDbUsername
      write setDbUsername;

    // Пароль пользователя DB
    function getDbPassword: WideString;
    procedure setDbPassword( password: WideString );
    property dbPassword : WideString
      read getDbPassword
      write setDbPassword;

    // Строка подключения к DB
    function getDbConnectionString: WideString;
    procedure setDbConnectionString( str:WideString );
    property dbConnectionString : WideString
      read getDbConnectionString
      write setDbConnectionString;
  end;

  // Сохранение конфигурации
  IConfigSave = interface
    // Сохранение настроек в файл config.ini в текущем каталоге
    // Ошибки
    //   В стучае ошибки генерирует исключение EConfigSave
    procedure Save();
  end;

  IConfig = interface(IDbConfig)
    // Возвращает кол-во ссылок - цель отладка
    function getRefCount: Integer;
  end;

  // Подписчик на изменения настроек
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
    // Имя пользователя DB
    dbUserNameValue : WideString;

    // Пароль пользователя DB
    dbPasswordValue : WideString;

    // Строка подключения к DB
    dbConnectionStringValue : WideString;

    // Формат времени
    dateFormatValue: WideString;

    // Формат времени для SQL
    sqlDateFormatValue: WideString;

    // Способ представления времени
    // см SQLDateParam
    dateToSqlMethodValue: WideString;

    // режим отладки
    debug: boolean;

    // Подписчики
    listeners: TList;

    // Экземпляры
    readers: TList;
  public
    // Создание конфига со значениями по умолчанию
    constructor Create();

    // Конструктор копирования
    // Аргументы
    //   - sample - пример для копирования
    constructor Copy( const sample: TConfig );

    // Сохранение настроек
    // Аргументы
    //  - fileName - имя файла
    // Ошибки
    //   В стучае ошибки генерирует исключение EConfigSave
    procedure Save( const fileName: WideString ); overload;

    // Сохранение настроек в файл config.ini в текущем каталоге
    // Ошибки
    //   В стучае ошибки генерирует исключение EConfigSave
    procedure Save(); overload;

    // Чтение настроек
    // Аргументы
    //  - fileName - имя файла
    // Ошибки
    //   В стучае ошибки генерирует исключение EConfigLoad
    procedure Load( const fileName: WideString ); overload;

    // Чтение настроек из файла config.ini в текущем каталоге
    // если файл отсуствуюет используется значения по умолчанию
    // Ошибки
    //   В стучае ошибки генерирует исключение EConfigLoad
    procedure Load(); overload;

    // свойство dbUsername
    function getDbUsername: WideString;
    procedure setDbUsername( userName: WideString );

    // свойство dbPassword
    function getDbPassword: WideString;
    procedure setDbPassword( password: WideString );

    // свойство dbConnectionString
    function getDbConnectionString: WideString;
    procedure setDbConnectionString( str:WideString );

    // свойство dateFormat
    // формат времени
    function getDateFormat: WideString;

    // свойство sqlDateFormat
    // формат времени используемый в SQL конструкциях  
    function getSqlDateFormat: WideString;

    // Способ представления времени
    // см SQLDateParam
    function getDateToSqlMethod: WideString;

    // кол-во ссылок
    function getRefCount(): Integer;

    function isDebug(): Boolean;

    // логгирование разрешено/запрещено
    function isLogEnabled: Boolean;

    // имя лог файла относительно текущего каталога
    function getLogFilename: WideString;

    // добавлять в лог файл записи или переписывать лог файл
    function isAppendLogFile: Boolean;

    // Добавляет подписчика на изменения
    procedure addListener( listener: TConfigListener );

    // Добавляет подписчика на чтение ini файла
    procedure addReader( listener: TConfigReader );
  end;

  // Ошибка сохранения конфига
  EConfigSave = class(Exception);

  // Ошибка чтения конфига
  EConfigLoad = class(Exception);

const
  DEFAULT_DB_USERNAME = 'username';
  DEFAULT_DB_CONNECTION_STRING = 'Provider=SQLOLEDB.1;'+
    'Persist Security Info=False;'+
    'User ID=username;'+
    'Initial Catalog=db_name;Data Source=host';
  DEFAULT_DB_PASSWORD = 'password';
  DEFAULT_CONFIG_FILENAME = 'config.init';

// Глобальные объекты
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

// свойство dbConnectionString

function TConfig.GetDbConnectionString: WideString;
begin
  result := self.dbConnectionStringValue;
end;

procedure TConfig.SetDbConnectionString(str: WideString);
begin
  self.dbConnectionStringValue := str;
end;

// свойство dbPassword

function TConfig.GetDbPassword: WideString;
begin
  result := self.dbPasswordValue;
end;

procedure TConfig.SetDbPassword(password: WideString);
begin
  self.dbPasswordValue := password;
end;

// свойство DbUsername

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
 