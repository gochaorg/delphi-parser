unit DBViewConfig;

interface

uses
  DBGrids, SysUtils,
  Loggers, Logging;

type
  // Подгатовка визуальных таблиц
  TDBViewConfig = class(TObject)
    constructor Create();

    // Подгатовка таблицы TDBGrid в зависимости от того где она используется
    // Аргументы
    //   className - имя класса контроллера
    //   grid - сетка данных
    procedure prepareGrid( const className:string; const grid:TDBGrid );
  private
    // Скрыть конкретные колонки
    // Аргументы
    //   grid - сетка данных
    //   name - имя колонки
    procedure hideColumn( const grid:TDBGrid; const name:string );

    // Скрывает колонки которые относятся в версии данных
    // Аргументы
    //   grid - сетка данных
    procedure hideVersionColumns( const grid:TDBGrid );

    // Устанавливает ширину колонки
    // Аргументы
    //   grid - сетка данных
    //   name - имя колонки
    //   width - ширина колонки
    procedure setColumnWidth( const grid:TDBGrid; const name:string; const width:Integer );

    // Устанавливает заголовок колонки
    // Аргументы
    //   grid - сетка данных
    //   name - имя колонки
    //   displayName - отображаемый текст колонки
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
    setColumnTitle(grid, 'name', 'Название');
  end;
  if className = CARS then begin
    hideColumn(grid, 'model_id');
    setColumnWidth(grid, 'legal_number', 130);
    setColumnWidth(grid, 'model_name', 150);

    hideColumn(grid, 'maintenance');
    setColumnTitle(grid, 'maintenance_s', 'Дата прохождения ТО');
    setColumnTitle(grid,'legal_number', 'Гос номер');
    setColumnTitle(grid,'model_name', 'Имя модели');
    setColumnTitle(grid,'wear', 'Пробег');
    setColumnTitle(grid, 'birth_year', 'Год выпуска');
  end;
  if className = DRIVERS then begin
    setColumnWidth(grid,'name',150);
    setColumnTitle(grid,'name','Имя водителя');
    setColumnWidth(grid,'birth_day_s',150);
    setColumnTitle(grid,'birth_day_s','Дата рождения');

    hideColumn(grid, 'birth_day');
  end;
  if className = DISPATCHERS then begin
    setColumnWidth(grid,'name',150);
    setColumnTitle(grid,'name','Имя диспетчера');
    setColumnWidth(grid,'birth_day_s',150);
    setColumnTitle(grid,'birth_day_s','Дата рождения');

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
    setColumnTitle(grid, 'car_legal_number', 'Гос номер');

    setColumnWidth(grid, 'driver_name', 130);
    setColumnTitle(grid, 'driver_name', 'Водитель');

    setColumnWidth(grid, 'dispatcher_name', 130);
    setColumnTitle(grid, 'dispatcher_name', 'Диспетчер');

    setColumnWidth(grid, 'outcome_date_s', 130);
    setColumnTitle(grid, 'outcome_date_s', 'Выезд');

    setColumnWidth(grid, 'income_date_s', 130);
    setColumnTitle(grid, 'income_date_s', 'Возврат');

    setColumnWidth(grid, 'car_model_name', 130);
    setColumnTitle(grid, 'car_model_name', 'Диспетчер');

    setColumnTitle(grid, 'fuel_cons', 'Расход топлива');
    setColumnTitle(grid, 'wear', 'Растрояние');

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
