unit OfficeExport;

interface

uses
  DBRows, Config;

type
  // ������� DBRows � ������� ����������
  IOfficeExport = interface
    // ��������� �������
    procedure doExport( dbRows:IDBRows );
  end;

var
  // ������� ������� � MS Excel
  excelExporter : IOfficeExport;

  // ������� ������� � MS Word
  wordExporter : IOfficeExport;

implementation

uses
  ExcelExport, WordExport, IniFiles, SysUtils;

type
TOfficeConfig = class(TConfigReader)
  procedure read(ini:TIniFile); override;
end;

{ TOfficeConfig }

// ����������
//   true - ���� ��������� ���� �������� ����������
//   fale - � ��������� ������� (�������������)
function isAbsolutePath( path:string ):boolean;
begin
  result := false;
  if length(path)>1 then begin
    if path[2] = ':' then begin
      result := true;
    end;
  end;
end;

// ����������� ���� � ����������
// ���������
//   path - ���� � �����, ���������� ��� �������������
// ���������� ���������� ����
function toAbsolutePath( path:string ):string;
begin
  if isAbsolutePath(path) then begin
    result := path;
  end else begin
    result := GetCurrentDir + '\' + path;
  end;
end;

// ������������� ������������
//
// ������ [excel]
// �������� template - ���� �������, �������� '-' (���� ��� �������) - ���������, ��� ������ ����������
//
// ������ [word]
// �������� template - ���� �������, �������� '-' (���� ��� �������) - ���������, ��� ������ ����������
// �������� insertInto - ��������� ��� �������� � �������, ���� ����� ��������� �������
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
