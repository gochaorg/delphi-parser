unit DriversFrame;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, Grids, DBGrids, ExtCtrls, StdCtrls, DB, ADODB,

  DBRows, DBRowPredicate, DBView, Map, DBRowsSqlExec,
  DBViewConfig,

  DriverForm, FormConfig,
  Loggers, Logging
  ;

type
  // Контроллер/фрейм списка водителей
  // Основные функции
  //   Отображение списка водителей
  //   Добавление информации о водителе
  //   Редактирование информации о водителе
  //   Удаление информации о водителях
  TDriversController = class(TFrame)
    Panel1: TPanel;
    driversDBGrid: TDBGrid;
    refreshButton: TButton;
    newButton: TButton;
    editButton: TButton;
    deleteButton: TButton;
    driversDataSource: TDataSource;
    driversADOQuery: TADOQuery;

    // Добавление новой записи
    procedure newButtonClick(Sender: TObject);

    // Редактирование выбранной записи
    procedure editButtonClick(Sender: TObject);
    
    // Удаление выбранных записей
    procedure deleteButtonClick(Sender: TObject);

    // Обновление списка отобрадаемых записей
    procedure refreshButtonClick(Sender: TObject);
  private
    { Private declarations }
  public
    // Подключение СУБД, активизация кнопок и прочего для управления
    procedure ActivateDataView();

    // Обновление текущей строки
    procedure RefreshCurrent();

    // Обновление всех записей
    procedure RefreshAll();
  end;

implementation

var
log:ILog;

{$R *.dfm}

procedure TDriversController.ActivateDataView();
begin
  log.println('ActivateDataView');
  driversADOQuery.Active := true;
  dbViewPreparer.prepareGrid(Self.ClassName, driversDBGrid);

  refreshButton.Enabled := true;
  newButton.Enabled := true;
  editButton.Enabled := true;
  deleteButton.Enabled := true;
end;

procedure TDriversController.RefreshCurrent();
begin
  log.println('RefreshCurrent');
  driversADOQuery.Refresh;
  driversDBGrid.Refresh;
end;

procedure TDriversController.RefreshAll();
begin
  log.println('RefreshAll');
  driversADOQuery.Active := false;
  driversADOQuery.Active := true;
  dbViewPreparer.prepareGrid(Self.ClassName, driversDBGrid);
end;


procedure TDriversController.newButtonClick(Sender: TObject);
var
  insertDialog : TDriverController;
begin
  log.println('newButtonClick');
  insertDialog := TDriverController.Create(self);
  FormConfigure(insertDialog);
  try
    if insertDialog.InsertDialog(driversADOQuery.Connection) then begin
      refreshAll;

      extend(driversDBGrid).SelectAndFocus(
        TDataRowValueEqualsPredicate.Create('id', insertDialog.getInsertedId)
      );

      driversDBGrid.SetFocus;
    end;
  finally
    FreeAndNil(insertDialog);
  end;
end;

procedure TDriversController.editButtonClick(Sender: TObject);
var
  curRow: TStringMap;
  updateDialog : TDriverController;
begin
  log.println('editButtonClick');
  curRow := TStringMap.Create;
  try
    if extend(driversDBGrid).GetFocusedRow(curRow) then begin
      updateDialog := TDriverController.Create(self);
      FormConfigure(updateDialog);
      try
        if updateDialog.UpdateDialog(
          driversADOQuery.Connection,
          curRow.get('id'),
          curRow.get('name'),
          curRow.get('birth_day_s')
        ) then begin
          RefreshCurrent;
        end;
      finally
        updateDialog.Close;
      end;
    end;
  finally
    FreeAndNil(curRow);
  end;
end;

procedure TDriversController.deleteButtonClick(Sender: TObject);
var
  rows: TDBRows;
  rowDelete:  TDBRowsSqlExec;
  query: TADOQuery;
begin
  log.println('deleteButtonClick');
  rows := TDBRows.Create;

  query := TADOQuery.Create(nil);
  query.Connection := driversADOQuery.Connection;
  query.SQL.Text := 'delete from drivers where [id] = :ID';

  rowDelete := TDBRowsSqlExec.Create(query);
  rowDelete.Map('id', 'id');
  try
    extend(driversDBGrid)
      .fetchActiveRecord(true)
      .fetchRows(true,false, rows.Add);

    rows.Each(rowDelete.Execute);
    if rowDelete.getErrorsCount > 0 then
      begin
        ShowMessage('В процессе удаления обнаружены ошибки');
      end
    else
      begin
        refreshAll;
      end;
  finally
    FreeAndNil(query);
    FreeAndNil(rows);
    FreeAndNil(rowDelete);
  end;
end;

procedure TDriversController.refreshButtonClick(Sender: TObject);
begin
  log.println('refreshButtonClick');
  RefreshAll;
end;

initialization
log := logger('DriversFrame');

end.
