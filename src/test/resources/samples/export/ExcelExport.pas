unit ExcelExport;

interface

uses
  OfficeExport, ComObj, SysUtils, Variants,

  DBRows, Map,
  Logging, Loggers;

type

IExcelExport = interface
end;

// Ёкспорт в excel
TExcelExport = class(TInterfacedObject,IOfficeExport,IExcelExport)
  private
    templateFile: WideString;
  public
    constructor Create;
    destructor Destroy; override;

    // ”казывает файл шаблона Excel
    // јргументы
    //   fileName - им€ файла шаблона, или пуста€ строка - что бы не использовать шаблон
    function withTemplate( fileName:WideString ):TExcelExport;
    procedure doExport( dbRows:IDBRows );
end;

implementation

var
log : ILog;

{ TExcelExport }

constructor TExcelExport.Create;
begin
  inherited Create;
end;

destructor TExcelExport.Destroy;
begin
  inherited Destroy;
end;

function TExcelExport.withTemplate(fileName: WideString): TExcelExport;
begin
  self.templateFile := fileName;
  result := self;
end;

procedure TExcelExport.doExport(dbRows: IDBRows);
var
  excelApp : OleVariant;
  doc : OleVariant;
  sheet : OleVariant;
  range : OleVariant;
  x,y,j : Integer;
///////////////////////////////////////////////////////////
  procedure headerExport();
  var
    i:Integer;
    col: TDBRowColumn;
    value: WideString;
  begin
    for i:=0 to (dbRows.GetColumnsCount-1) do begin
      if dbRows.GetColumn(i,col) then begin
        value := col.Title;

        log.println(
          'range.Cells['+IntToStr(y)+','+IntToStr(x+i)+'] := '+
          value
        );

        range.Cells[y,x+i] := value;
      end;
    end;
  end;
//-----------------------------
  procedure nextRow();
  begin
    y := y + 1;
  end;
//-----------------------------
  procedure dataRowExport( rowIndex:Integer );
  var
    i:Integer;
    data:TStringMap;
    column: TDBRowColumn;
    value: variant;
  begin
    if dbRows.GetItem(rowIndex,data) then begin
      for i:=0 to (dbRows.GetColumnsCount-1) do begin
        if dbRows.GetColumn(i,column) then begin
          if data.exists(column.Name) then begin
            value := data.get(column.Name);

            log.println(
              'range.Cells['+IntToStr(y)+','+IntToStr(x+i)+'] := '+
              VarToStr(value)
            );
            
            range.Cells[y,x+i] := value;
          end;
        end;
      end;
    end;
  end;
///////////////////////////////////////////////////////////
begin
  log.println('excelApp := CreateOleObject(''Excel.Application'')');
  excelApp := CreateOleObject('Excel.Application');

  log.println('excelApp.Visible := true');
  excelApp.Visible := true;

  if length(self.templateFile)>0 then begin
    log.println('excelApp.Workbooks.Add '+self.templateFile);
    doc := excelApp.Workbooks.Add(self.templateFile);
  end else begin
    log.println('excelApp.Workbooks.Add');
    doc := excelApp.Workbooks.Add();
  end;

  log.println('sheet := excelApp.ActiveSheet');
  sheet := excelApp.ActiveSheet;

  log.println('sheet.Range[''A1'']');
  range := sheet.Range['A1'];

  x := 1;
  y := 1;

  headerExport;
  for j:=0 to (dbRows.GetCount-1) do begin
    nextRow;
    dataRowExport(j);
  end;
end;

initialization
log := logger('ExcelExport');

end.
