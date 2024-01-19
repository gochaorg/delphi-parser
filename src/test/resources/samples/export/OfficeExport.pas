unit OfficeExport;

interface

uses
  DBRows, Config;

type
  // Ёкспорт DBRows в офисные приложени€
  IOfficeExport = interface
    // ¬ыполнить экспорт
    procedure doExport( dbRows:IDBRows );
  end;

var
  // Ёкспорт таблицы в MS Excel
  excelExporter : IOfficeExport;

  // Ёкспорт таблицы в MS Word
  wordExporter : IOfficeExport;

implementation

uses
  ExcelExport, WordExport, IniFiles, SysUtils;

type
TOfficeConfig = class(TConfigReader)
  procedure read(ini:TIniFile); override;
end;

{ TOfficeConfig }

// ¬озвразает
//   true - если указанный путь €вл€етс€ абсолютным
//   fale - в остальных случа€х (относительный)
function isAbsolutePath( path:string ):boolean;
begin
  result := false;
  if length(path)>1 then begin
    if path[2] = ':' then begin
      result := true;
    end;
  end;
end;

//  онвертаци€ пути в абсолютный
// јргументы
//   path - путь к файлу, абсолютный или относительный
// ¬озвращает абсолютный путь
function toAbsolutePath( path:string ):string;
begin
  if isAbsolutePath(path) then begin
    result := path;
  end else begin
    result := GetCurrentDir + '\' + path;
  end;
end;

// »нициализаци€ конфигурации
//
// секци€ [excel]
// значение template - путь шаблону, значение '-' (тире без ковычек) - указывает, что шаблон отсуствует
//
// секци€ [word]
// значение template - путь шаблону, значение '-' (тире без ковычек) - указывает, что шаблон отсуствует
// значение insertInto - указывает им€ закладки в шаблоне, куда будет добавлена таблица
procedure TOfficeConfig.read(ini: TIniFile);
var
  template: string;
  bookmark: string;
begin
  template := ini.ReadString('excel', 'template', '-');
  if not (template = '-') then begin
    excelExporter := TExcelExport.Create
      .withTemplate(toAbsolutePath(template));
  end;

  template := ini.ReadString('word', 'template', '-');
  if not (template = '-') then begin
    bookmark := ini.ReadString('word', 'insertInto', '');

    wordExporter := TWordExport.Create
      .withTemplate(toAbsolutePath(template))
      .withInsertIntoBookmark(bookmark);
  end;
end;

var
configReader : TOfficeConfig;

initialization

excelExporter := TExcelExport.Create;
wordExporter := TWordExport.Create;

configReader := TOfficeConfig.Create;
applicationConfigObj.addReader(configReader);

end.
