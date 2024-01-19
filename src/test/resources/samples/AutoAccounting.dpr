program AutoAccounting;

uses
  Forms,
  Dialogs,
  Config in 'Config.pas',
  Logging in 'log\Logging.pas',
  Loggers in 'log\Loggers.pas',
  Map in 'Map.pas',
  IntegerList in 'IntegerList.pas',
  DbConfForm in 'ui\dbConf\DbConfForm.pas' {DbConfController},
  CarsFrame in 'ui\cars\CarsFrame.pas' {CarsController: TFrame},
  CarsModelsFrame in 'ui\carsModel\CarsModelsFrame.pas' {CarsModelsController: TFrame},
  CarModelFrame in 'ui\carsModel\CarModelFrame.pas' {CarModelController},
  MainFormController in 'ui\main\MainFormController.pas' {MainForm},
  CarForm in 'ui\cars\CarForm.pas' {CarController},
  DispatchersFrame in 'ui\dispatcher\DispatchersFrame.pas' {DispatchersController: TFrame},
  DriversFrame in 'ui\drivers\DriversFrame.pas' {DriversController: TFrame},
  WaybillsFrame in 'ui\waybills\WaybillsFrame.pas' {waybillsController: TFrame},
  DriverForm in 'ui\drivers\DriverForm.pas' {DriverController},
  DriverSQL in 'ui\drivers\DriverSQL.pas',
  MyDate in 'time\MyDate.pas',
  CarSQL in 'ui\cars\CarSQL.pas',
  DispatcherSQL in 'ui\dispatcher\DispatcherSQL.pas',
  DispatcherForm in 'ui\dispatcher\DispatcherForm.pas' {DispatcherController},
  WaybillForm in 'ui\waybills\WaybillForm.pas' {WaybillController},
  WaybillSQL in 'ui\waybills\WaybillSQL.pas',
  DMLOperation in 'db\DMLOperation.pas',
  Validation in 'validate\Validation.pas',
  MyDateTime in 'MyDateTime.pas',
  DBRowPredicate in 'db\DBRowPredicate.pas',
  DBRows in 'db\DBRows.pas',
  DBRowsLogger in 'db\DBRowsLogger.pas',
  DBRowsSqlExec in 'db\DBRowsSqlExec.pas',
  DBView in 'db\DBView.pas',
  DBViewConfig in 'db\DBViewConfig.pas',
  WaybillSQLView in 'ui\waybills\WaybillSQLView.pas',
  OfficeExport in 'export\OfficeExport.pas',
  ExcelExport in 'export\ExcelExport.pas',
  WordExport in 'export\WordExport.pas',
  SQLDateParam in 'time\SQLDateParam.pas',
  FormConfig in 'ui\FormConfig.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(TMainForm, MainForm);
  try
    applicationConfigObj.Load();
    logger('app').println('started');
  except
    on e: EConfigLoad do begin
      ShowMessage('can''t read config: ' + e.Message);
    end;
  end;

  Application.Run;
end.
