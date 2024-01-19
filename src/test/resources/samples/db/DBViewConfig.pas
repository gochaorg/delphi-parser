unit DBViewConfig;

interface

uses
  DBGrids, SysUtils,
  Loggers, Logging;

type
  // ���������� ���������� ������
  TDBViewConfig = class(TObject)
    constructor Create();

    // ���������� ������� TDBGrid � ����������� �� ���� ��� ��� ������������
    // ���������
    //   className - ��� ������ �����������
    //   grid - ����� ������
    procedure prepareGrid( const className:string; const grid:TDBGrid );
  private
    // ������ ���������� �������
    // ���������
    //   grid - ����� ������
    //   name - ��� �������
    procedure hideColumn( const grid:TDBGrid; const name:string );

    // �������� ������� ������� ��������� � ������ ������
    // ���������
    //   grid - ����� ������
    procedure hideVersionColumns( const grid:TDBGrid );

    // ������������� ������ �������
    // ���������
    //   grid - ����� ������
    //   name - ��� �������
    //   width - ������ �������
    procedure setColumnWidth( const grid:TDBGrid; const name:string; const width:Integer );

    // ������������� ��������� �������
    // ���������
    //   grid - ����� ������
    //   name - ��� �������
    //   displayName - ������������ ����� �������
    procedure setColumnTitle(
      const grid:TDBGrid;
      const name:string;
      const displayName:WideString
    );
  end;

var
  dbViewPreparer : TDBViewConfig;
  log: ILog;

implementation

const
  CARS_MODEL = 'TCarsModelsController';
  CARS = 'TCarsController';
  WAYBILLS = 'TWaybillsController';
  DRIVERS = 'TDriversController';
  DISPATCHERS = 'TDispatchersController';

constructor TDBViewConfig.Create;
begin
  inherited Create();
end;

procedure TDBViewConfig.prepareGrid(
  const className:string;
  const grid: TDBGrid
);
begin
  log.println('prepareGrid '+className);
  if className = CARS_MODEL then begin
    hideVersionColumns(grid);
    setColumnWidth(grid, 'name', 150);
    setColumnTitle(grid, 'name', '��������');
  end;
  if className = CARS then begin
    hideColumn(grid, 'model_id');
    setColumnWidth(grid, 'legal_number', 130);
    setColumnWidth(grid, 'model_name', 150);

    hideColumn(grid, 'maintenance');
    setColumnTitle(grid, 'maintenance_s', '���� ����������� ��');
    setColumnTitle(grid,'legal_number', '��� �����');
    setColumnTitle(grid,'model_name', '��� ������');
    setColumnTitle(grid,'wear', '������');
    setColumnTitle(grid, 'birth_year', '��� �������');
  end;
  if className = DRIVERS then begin
    setColumnWidth(grid,'name',150);
    setColumnTitle(grid,'name','��� ��������');
    setColumnWidth(grid,'birth_day_s',150);
    setColumnTitle(grid,'birth_day_s','���� ��������');

    hideColumn(grid, 'birth_day');
  end;
  if className = DISPATCHERS then begin
    setColumnWidth(grid,'name',150);
    setColumnTitle(grid,'name','��� ����������');
    setColumnWidth(grid,'birth_day_s',150);
    setColumnTitle(grid,'birth_day_s','���� ��������');

    hideColumn(grid, 'birth_day');
  end;
  if className = WAYBILLS then begin
    hideColumn(grid, 'car_id');
    hideColumn(grid, 'car_model_id');
    hideColumn(grid, 'driver_id');
    hideColumn(grid, 'dispatcher_id');
    hideColumn(grid, 'outcome_date');
    hideColumn(grid, 'income_date');
    hideColumn(grid, 'car_total_wear');
    hideColumn(grid, 'search_text');
    hideColumn(grid, 'state');

    setColumnWidth(grid, 'car_legal_number', 130);
    setColumnTitle(grid, 'car_legal_number', '��� �����');

    setColumnWidth(grid, 'driver_name', 130);
    setColumnTitle(grid, 'driver_name', '��������');

    setColumnWidth(grid, 'dispatcher_name', 130);
    setColumnTitle(grid, 'dispatcher_name', '���������');

    setColumnWidth(grid, 'outcome_date_s', 130);
    setColumnTitle(grid, 'outcome_date_s', '�����');

    setColumnWidth(grid, 'income_date_s', 130);
    setColumnTitle(grid, 'income_date_s', '�������');

    setColumnWidth(grid, 'car_model_name', 130);
    setColumnTitle(grid, 'car_model_name', '���������');

    setColumnTitle(grid, 'fuel_cons', '������ �������');
    setColumnTitle(grid, 'wear', '����������');

  end;
end;

procedure TDBViewConfig.setColumnTitle(
  const grid:TDBGrid;
  const name:string;
  const displayName:WideString
);
var
  tcol : TColumn;
  ci : Integer;
begin
  for ci := 0 to (grid.Columns.Count-1) do begin
    tcol := grid.Columns[ci];
    if SameText(tcol.FieldName,name) then begin
      tcol.Title.Caption := displayName;
    end;
  end;
end;


procedure TDBViewConfig.hideVersionColumns(const grid: TDBGrid);
var
  tcol : TColumn;
  ci : Integer;
begin
  for ci := 0 to (grid.Columns.Count-1) do begin
    tcol := grid.Columns[ci];
    if SameText(tcol.FieldName,'ValidFrom') then begin tcol.Visible := false; end;
    if SameText(tcol.FieldName,'ValidTo')   then begin tcol.Visible := false; end;
  end;
end;

procedure TDBViewConfig.setColumnWidth( const grid:TDBGrid; const name:string; const width:Integer );
var
  tcol : TColumn;
  ci : Integer;
begin
  for ci := 0 to (grid.Columns.Count-1) do begin
    tcol := grid.Columns[ci];
    if SameText(tcol.FieldName,name) then begin tcol.Width := width; end;
  end;
end;

procedure TDBViewConfig.hideColumn
( const grid: TDBGrid;
  const name: string
);
var
  tcol : TColumn;
  ci : Integer;
begin
  for ci := 0 to (grid.Columns.Count-1) do begin
    tcol := grid.Columns[ci];
    if SameText(tcol.FieldName,name) then begin tcol.Visible := false; end;
  end;
end;

initialization
  dbViewPreparer := TDBViewConfig.Create();
  log := logger('DBViewConfig');

end.
