unit DBView;

interface

uses
  DBGrids, DB,
  Classes, SysUtils,

  Config,
  DBRowPredicate, DBRows,
  Logging, Loggers, Map;

type
  // ������������ ��� �����������
  TDataRowSelectionUpdate = class(TObject)
    private
      index: Integer;
      row: TStringMap;
      hasSelectedValue: boolean;
      setSelectedValue: boolean;
      hasFocusValue: boolean;
      setFocusValue: boolean;
    public
      // ��������
      //   row - ������ ������, ������� ���� ��������������
      //   index - ������ ������
      constructor Create(
        row:TStringMap;
        index:Integer;
        hasSelection:boolean;
        hasFocus:boolean
      );
      destructor Destory;

      // ���������� ������� ������ (������)
      function getRow:TStringMap; virtual;

      // ������� ������ ������� ?
      function isSelected:boolean; virtual;

      // ���������� ������ ��� ���������
      procedure setSelect(selected:boolean); virtual;

      // ��� ������� ������ ������������ �������� ?
      function isSelectChanged:boolean; virtual;

      // ������� ������ �������� �����
      function hasFocus:boolean; virtual;

      // ���������� ����� �� ������
      procedure setFocus(focus:boolean); virtual;

      // ��� ������� ������ ������� ������� �����
      function isFocusChanged:boolean; virtual;
  end;

  // ������� ���������� ������
  // ������ �� ���������� �� ��������
  TDataRowConsumer  = procedure (row:TStringMap) of object;

  // ������� ���������� ������
  // ������ �� ���������� �� ��������
  TDataRowConsumerI = procedure (row:IStringMap) of object;

  // ������� ����������� ��������� ������
  TDataRowSelectUpdater = procedure (row:TDataRowSelectionUpdate) of object;

  // ������� ����������� ���������� � �������
  // ������� �� ���������� �� ��������
  TDataColumnConsumer = procedure (column:TDBRowColumn) of object;

  /////////////////////////////////////////////////////////////
  // ���������� ������� �� ������ � grid
  IDBGridExtension = interface
    // ���������� ���-�� ����� � TDBGrid
    function GetRowsCount(): Integer;

    // ������� �����
    // ���������
    //   selected - ���������� ������
    //   unselected - �� ���������� ������
    //   consumer - ��������, �� TDBRows.add
    procedure FetchRows(
      selected: Boolean;
      unselected:Boolean;
      consumer:TDataRowConsumer
    );

    // �������� � ������������� ����� �� ��������� ������
    procedure SelectAndFocus( predicate: IDataRowPredicate );

    // �������� �������� ������ ���������� �����
    // ���������
    //   row - ������ �� ������
    // ���������
    //   true - ������ ������� ��������
    //   false - ������� ������ �� �������
    function GetFocusedRow( var row:TStringMap ): boolean;

    // �������� �������� ������� ������
    // ���������
    //   row - ������ �� ������
    // ���������
    //   true - ������ ������� ��������
    //   false - ������� ������ �� �������
    function GetCurrentRow( var row:TStringMap ): boolean;

    // ��������� ����� ������ �������� (FetchRow)
    // ���������
    //   visibleColumn - ������� �������
    //   inVisibleColumn - ��������� �������
    function FetchVisible(
      visibleColumn: boolean;
      inVisibleColumn: boolean
    ):IDBGridExtension;

    // ��������� �������� �������� ������ ��� ���
    // ���������
    //   activeRecord - true - �������� �������� ������
    function FetchActiveRecord(
      activeRecord:boolean
    ):IDBGridExtension;

    // ������� �����
    function GetDBRows:IDBRows;
  end;

  /////////////////////////////////////////////////////////////
  // �������������� ������� �� ������ � grid
  TDBGridExt = class(TInterfacedObject, IDBGridExtension)
    private
      grid: TDBGrid;
      visibleColumnsFetch: boolean;
      inVisibleColumnsFetch: boolean;
      activeRecordFetch: boolean;
    public
      constructor Create( const grid:TDBGrid );
      function Ext(): IDBGridExtension;
      destructor Destroy; override;

      function GetRowsCount(): Integer; virtual;

      function FetchVisible(
        visibleColumn: boolean;
        inVisibleColumn: boolean
      ):IDBGridExtension;

      procedure FetchRows(
        selected: Boolean;
        unselected:Boolean;
        consumer:TDataRowConsumer
      );

      function FetchActiveRecord(
        activeRecord:boolean
      ):IDBGridExtension;

      procedure UpdateSelection(
        updater: TDataRowSelectUpdater;
        columnConsumer: TDataColumnConsumer = nil
      );

      function GetDBRows:IDBRows; 

      procedure SelectAndFocus( predicate: IDataRowPredicate );

      function GetFocusedRow( var row:TStringMap ): boolean;

      function GetCurrentRow( var row:TStringMap ): boolean;
  end;

  //////////////////////////////////////////////////////////////
  // ���������� ������� �� ������ � grid
  function extend( const grid: TDBGrid ): IDBGridExtension;

implementation

uses
  Dialogs, Variants;

var
  log : ILog;

type
  // ���������� ���������� ����� � ������ ���������� �����
  TSetSelectAndFocusUpdater = class(TObject)
    private
      predicate: IDataRowPredicate;
      setFocus: boolean;
      setSelect: boolean;
    public
      // �����������
      // ���������
      //   setFocus - ���������� ����� ��������������� �������
      //   setSelect - ��������� ��������� ��������������� �������
      //   predicate - ������� �� �������� ���������� ������
      constructor Create( setFocus:boolean; setSelect:boolean; predicate:IDataRowPredicate );
      destructor Destroy; override;
      procedure Update(row:TDataRowSelectionUpdate);
  end;

  // ���������� ����� �� TDataRowSelectUpdater � TDataRowConsumer
  TFetchRowDelegate = class(TObject)
    private
      selected: boolean;
      unSelected: boolean;
      target : TDataRowConsumer;
      activeRecord:boolean;
    public
      // �����������
      // ���������
      //   selected - ������������ ��������� ������
      //   unSelected - ������������ �� ��������� ������
      //   target - ���� ������������ �����
      constructor Create(
        selected:boolean;
        unSelected:boolean;
        activeRecord:boolean;
        target : TDataRowConsumer
      );
      destructor Destroy; override;
      procedure Consume( row: TDataRowSelectionUpdate );
  end;

  // ������ ��������� ����� (�������)
  TDBRowsBuilder = class(TObject)
    private
      dbRows : TDBRows;
    public
      constructor Create( dbRows:TDBRows );
      destructor Destroy; override;

      // ��������� ������ ������
      procedure RowConsume( row: TDataRowSelectionUpdate );

      // ��������� ������� ������
      procedure ColumnConsume( column:TDBRowColumn );
  end;

{ TDBGridExt }

constructor TDBGridExt.Create(const grid: TDBGrid);
begin
  inherited Create();
  self.grid := grid;
  self.visibleColumnsFetch := true;
  self.inVisibleColumnsFetch := true;
end;


destructor TDBGridExt.Destroy;
begin
  self.grid := nil;
  inherited Destroy;
end;

function TDBGridExt.Ext: IDBGridExtension;
begin
  result := self;
end;

procedure TDBGridExt.FetchRows(
  selected, unselected: Boolean;
  consumer: TDataRowConsumer
);
var
  delegate : TFetchRowDelegate;
begin
  delegate := TFetchRowDelegate.Create(
    selected,
    unselected,
    self.activeRecordFetch,
    consumer);
  try
    self.UpdateSelection(delegate.Consume);
  finally
    FreeAndNil(delegate);
  end;
end;

function TDBGridExt.FetchVisible(
  visibleColumn,
  inVisibleColumn: boolean
): IDBGridExtension;
begin
  self.visibleColumnsFetch := visibleColumn;
  self.inVisibleColumnsFetch := inVisibleColumn;
  result := self;
end;

function TDBGridExt.FetchActiveRecord( activeRecord:boolean ): IDBGridExtension;
begin
  self.activeRecordFetch := activeRecord;
  result := self;
end;

function TDBGridExt.GetCurrentRow(var row: TStringMap): boolean;
var
  c : Integer;
begin
  result := false;
  if assigned(self.grid) then begin
    if assigned(self.grid.DataSource) then begin
      if assigned(self.grid.DataSource.DataSet) then begin
        for c:=0 to self.grid.DataSource.DataSet.Fields.Count-1 do begin
          row.put(
            self.grid.Columns.Items[c].FieldName,
            self.grid.Fields[c].Value
          );
        end;
        result := true;
      end;
    end;
  end;
end;

function TDBGridExt.GetDBRows: IDBRows;
var
  dbRows : TDBRows;
  dbRowsBuilder : TDBRowsBuilder;
begin
  dbRows := TDBRows.Create;
  dbRowsBuilder := TDBRowsBuilder.Create(dbRows);
  self.UpdateSelection(
    dbRowsBuilder.RowConsume,
    dbRowsBuilder.ColumnConsume
  );
  result := dbRows;
end;

function TDBGridExt.GetFocusedRow(var row: TStringMap): boolean;
var
  c : Integer;
begin
  result := false;
  if assigned(self.grid) then begin
    if assigned(self.grid.DataSource) then begin
      if assigned(self.grid.DataSource.DataSet) then begin
        if self.grid.DataSource.DataSet.RecNo>0 then begin
          for c:=0 to self.grid.DataSource.DataSet.Fields.Count-1 do begin
            row.put(
              self.grid.Columns.Items[c].FieldName,
              self.grid.Fields[c].Value
            );
          end;
          result := true;
        end;
      end;
    end;
  end;
end;

function TDBGridExt.GetRowsCount: Integer;
begin
  result := 0;
  if assigned(self.grid) then begin
    if assigned(self.grid.DataSource) then begin
      if assigned(self.grid.DataSource.DataSet) then begin
        result := self.grid.DataSource.DataSet.RecordCount;
      end;
    end;
  end;
end;

function extend( const grid: TDBGrid ): IDBGridExtension;
begin
  result := TDBGridExt.Create(grid).Ext;
end;

procedure TDBGridExt.SelectAndFocus(predicate: IDataRowPredicate);
var
  updater : TSetSelectAndFocusUpdater;
begin
  updater := TSetSelectAndFocusUpdater.Create(true,true,predicate);
  try
    UpdateSelection(updater.Update);
  finally
    FreeAndNil(updater);
  end;
end;

procedure TDBGridExt.UpdateSelection(
  updater: TDataRowSelectUpdater;
  columnConsumer: TDataColumnConsumer = nil
);
var
  bm: TBookmark;
  i,c: Integer;
  row: TStringMap;
  rowUpdate: TDataRowSelectionUpdate;
  savedActiveRecNo: Integer;
  restoreActiveRecNo: Integer;
  column: TDBRowColumn;
begin
  if assigned(self.grid) then begin
    if assigned(self.grid.DataSource) then begin
      if assigned(self.grid.DataSource.DataSet) then begin
        savedActiveRecNo := self.grid.DataSource.DataSet.RecNo;
        restoreActiveRecNo := savedActiveRecNo;
        bm := self.grid.DataSource.DataSet.GetBookmark;
        self.grid.DataSource.DataSet.DisableControls;
        self.grid.DataSource.DataSet.First;

        // ����������� ������ �������
        if assigned(columnConsumer) then begin
          for c:=0 to self.grid.DataSource.DataSet.Fields.Count-1 do begin
            column := TDBRowColumn.Create;
            try
              column.Name  := self.grid.Columns.Items[c].FieldName;
              column.Title := self.grid.Columns.Items[c].Title.Caption;
              column.Visible := self.grid.Columns.Items[c].Visible;
              column.Width := self.grid.Columns.Items[c].Width;
              columnConsumer(column);
            finally
              FreeAndNil(column);
            end;
          end;
        end;

        // ����� �����
        try
          for i:=0 to (self.grid.DataSource.DataSet.RecordCount-1) do begin
            row := TStringMap.Create;

            if applicationConfigObj.isDebug then
              log.println(
                'row#'+IntToStr(i)+
                ' grid.SelectedRows.CurrentRowSelected='+
                  BoolToStr(self.grid.SelectedRows.CurrentRowSelected)+
                ' savedActiveRecNo='+BoolToStr(savedActiveRecNo = (i+1))
              );

            rowUpdate := TDataRowSelectionUpdate.Create(
              row,
              i,
              self.grid.SelectedRows.CurrentRowSelected,
              savedActiveRecNo = (i+1)
            );
            try

              // build data
              for c:=0 to (self.grid.DataSource.DataSet.Fields.Count-1) do begin
                if  (   self.visibleColumnsFetch
                    and self.grid.Columns.Items[c].Visible
                    ) or
                    (   self.inVisibleColumnsFetch
                    and not self.grid.Columns.Items[c].Visible
                    )
                then
                  row.put(
                    self.grid.Columns.Items[c].FieldName,
                    self.grid.Fields[c].Value
                  );
              end;
              updater( rowUpdate );
              if rowUpdate.isFocusChanged then begin
                restoreActiveRecNo := (i+1);
              end;
              if rowUpdate.isSelectChanged then begin
                self.grid.SelectedRows.CurrentRowSelected := rowUpdate.isSelected;
              end;
            finally
              FreeAndNil(rowUpdate);
              FreeAndNil(row);
              self.grid.DataSource.DataSet.Next;
            end;
          end;
        finally
          self.grid.DataSource.DataSet.EnableControls;
          if restoreActiveRecNo = savedActiveRecNo then
            begin
              self.grid.DataSource.DataSet.GotoBookmark(bm);
            end
          else
            begin
              if restoreActiveRecNo > 0 then
              begin
                self.grid.DataSource.DataSet.RecNo := restoreActiveRecNo;
              end;
            end;
          self.grid.DataSource.DataSet.FreeBookmark(bm);
        end;
      end;
    end;
  end;
end;

{ TDataRowSelectionUpdate }

constructor TDataRowSelectionUpdate.Create(
  row: TStringMap;
  index: Integer;
  hasSelection:boolean;
  hasFocus:boolean
);
begin
  inherited Create();
  self.row := row;
  self.index := index;
  self.hasSelectedValue := hasSelection;
  self.setSelectedValue := hasSelection;
  self.hasFocusValue := hasFocus;
  self.setFocusValue := hasFocus;
end;

destructor TDataRowSelectionUpdate.Destory;
begin
  self.row := nil;
  inherited Destroy();
end;

function TDataRowSelectionUpdate.getRow: TStringMap;
begin
  result := self.row;
end;

function TDataRowSelectionUpdate.hasFocus: boolean;
begin
  result := self.hasFocusValue;
end;

function TDataRowSelectionUpdate.isFocusChanged: boolean;
begin
  result := self.hasFocusValue <> self.setFocusValue;
end;

function TDataRowSelectionUpdate.isSelectChanged: boolean;
begin
  result := self.hasSelectedValue <> self.setSelectedValue;
end;

function TDataRowSelectionUpdate.isSelected: boolean;
begin
  result := self.hasSelectedValue;
end;

procedure TDataRowSelectionUpdate.setFocus(focus: boolean);
begin
  self.setFocusValue := true;
end;

procedure TDataRowSelectionUpdate.setSelect(selected: boolean);
begin
  self.setSelectedValue := true;
end;

{ TSetSelectAndFocusUpdater }

constructor TSetSelectAndFocusUpdater.Create(setFocus, setSelect: boolean;
  predicate: IDataRowPredicate);
begin
  self.predicate := predicate;
  self.setFocus := setFocus;
  self.setSelect := setSelect;
end;

destructor TSetSelectAndFocusUpdater.Destroy;
begin
  self.predicate := nil;
end;

procedure TSetSelectAndFocusUpdater.Update(row: TDataRowSelectionUpdate);
var
  matched: boolean;
begin
  matched := self.predicate.test(row.getRow);
  row.setFocus(matched);
  row.setSelect(matched);
end;

{ TFetchRowDelegate }

constructor TFetchRowDelegate.Create(
  selected:boolean;
  unSelected:boolean;
  activeRecord:boolean;
  target: TDataRowConsumer
);
begin
  inherited Create;
  self.target := target;
  self.selected := selected;
  self.unSelected := unSelected;
  self.activeRecord := activeRecord;
end;

destructor TFetchRowDelegate.Destroy;
begin
  self.target := nil;
  inherited Destroy;
end;

procedure TFetchRowDelegate.Consume(row: TDataRowSelectionUpdate);
begin
  if applicationConfigObj.isDebug then
    log.println('TFetchRowDelegate.Consume '+
      ' row.isSelected='+BoolToStr(row.isSelected)+
      ' row.hasFocus='+BoolToStr(row.hasFocus)+
      ' self.selected='+BoolToStr(self.selected)+
      ' self.unSelected='+BoolToStr(self.unSelected)+
      ' self.activeRecord='+BoolToStr(self.activeRecord)
    );

  if assigned(self.target) then begin
    if row.isSelected and self.selected
      then begin
        if applicationConfigObj.isDebug then log.println('  delegate');
        self.target(row.getRow);
      end
    else if not row.isSelected and self.unSelected
      then begin
        if applicationConfigObj.isDebug then log.println('  delegate');
        self.target(row.getRow);
      end
    else if row.hasFocus and self.activeRecord
      then begin
        if applicationConfigObj.isDebug then log.println('  delegate');
        self.target(row.getRow);
      end;
  end;
end;

{ TDBRowsBuilder }

constructor TDBRowsBuilder.Create( dbRows:TDBRows );
begin
  self.dbRows := dbRows;
  inherited Create;
end;

destructor TDBRowsBuilder.Destroy;
begin
  self.dbRows := nil;
  inherited Destroy;
end;

procedure TDBRowsBuilder.ColumnConsume(column: TDBRowColumn);
var
  columnInstance: TDBRowColumn;
begin
  if assigned(column) and assigned(self.dbRows) then begin
    columnInstance := dbRows.AddOrGetColumn(column.Name);
    columnInstance.Title   := column.Title;
    columnInstance.Visible := column.Visible;
    columnInstance.Width   := column.Width;
  end;
end;

procedure TDBRowsBuilder.RowConsume(row: TDataRowSelectionUpdate);
begin
  if assigned(self.dbRows) then begin
    self.dbRows.Add(row.getRow);
  end;
end;

initialization
log := logger('DBRows');

end.
