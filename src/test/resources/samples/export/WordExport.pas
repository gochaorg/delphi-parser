unit WordExport;

interface

uses
  OfficeExport, ComObj, SysUtils, Variants,

  DBRows, Map,
  Logging, Loggers;

type
IWordExport = interface
end;

// Ёкспорт в Word
TWordExport = class(TInterfacedObject,IOfficeExport,IWordExport)
  private
    templateFile: WideString;
    insertAtBookmark: WideString;
  public
    constructor Create;
    destructor Destroy; override;

    // ”казывает файл шаблона Word
    // јргументы
    //   fileName - им€ файла шаблона, или пуста€ строка - что бы не использовать шаблон
    function withTemplate( fileName:WideString ):TWordExport;

    // ”казывает им€ закладки 
    // јргументы
    //   bookmarkName - им€ закладки в шаблоне, куда будет добавлена таблица
    //                  или пуста€ строка, что бы не использовать закладку
    function withInsertIntoBookmark( bookmarkName:WideString ):TWordExport;
    procedure doExport( dbRows:IDBRows );
end;

implementation

var
log : ILog;

constructor TWordExport.Create;
begin
  inherited Create;
end;

destructor TWordExport.Destroy;
begin
  inherited Destroy;
end;

function TWordExport.withTemplate(fileName: WideString): TWordExport;
begin
  self.templateFile := fileName;
  result := self;
end;

function TWordExport.withInsertIntoBookmark(
  bookmarkName: WideString): TWordExport;
begin
  self.insertAtBookmark := bookmarkName;
  result := self;
end;

procedure TWordExport.doExport(dbRows: IDBRows);
var
  wordApp : OleVariant;
  doc : OleVariant;
  table : OleVariant;
  range: OleVariant;
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
          'table.Cell('+IntToStr(y)+','+IntToStr(x+i)+').Range.Text := '+
          VarToStr(value)
        );

        table.Cell(y,x+i).Range.Text := value;
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
              'table.Cell('+IntToStr(y)+','+IntToStr(x+i)+').Range.Text := '+
              VarToStr(value)
            );

            table.Cell(y,x+i).Range.Text := value;
          end;
        end;
      end;
    end;
  end;
///////////////////////////////////////////////////////////
begin
  log.println('wordApp := CreateOleObject(''Word.Application'')');
  wordApp := CreateOleObject('Word.Application');

  log.println('wordApp.Visible := true');
  wordApp.Visible := true;

  if length(self.templateFile)>0 then begin
    log.println('doc := wordApp.Documents.Add '+self.templateFile);
    doc := wordApp.Documents.Add(self.templateFile);
  end else begin
    log.println('doc := wordApp.Documents.Add');
    doc := wordApp.Documents.Add();
  end;

  if length(self.insertAtBookmark)>0 then begin
    log.println('range := doc.Bookmarks.Item('+self.insertAtBookmark+').Range');
    try
      range := doc.Bookmarks.Item(self.insertAtBookmark).Range;
    except
      on e:EOleException do begin
        log.println('! bookmark '+self.insertAtBookmark+
          ' возможно указан не правильно: '+e.Message,
        );

        log.println('range := doc.Range(0,0)');
        range := doc.Range(0,0);
      end;
    end;
  end else begin
    log.println('range := doc.Range(0,0)');
    range := doc.Range(0,0);
  end;

  log.println('doc.Documents.Add.Tables.Add( range, '+
    IntToStr(dbRows.GetCount)+
    ', '+
    IntToStr(dbRows.GetColumnsCount + 1)+
    ' )'
  );

  table := doc.Tables.Add( range,
    dbRows.GetCount,
    dbRows.GetColumnsCount + 1
    );

  x := 1;
  y := 1;

  headerExport;
  for j:=0 to (dbRows.GetCount-1) do begin
    nextRow;
    dataRowExport(j);
  end;
end;

initialization
log := logger('WordExport');

end.
