unit CarsFrame;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, DB, ExtCtrls, Grids, DBGrids, ADODB,

  FormConfig,
  Logging, Loggers,
  DBRows, DBRowPredicate, DBView, Map, DBRowsSqlExec,
  DBViewConfig,

  CarForm, CarSQL     
  ;

type
  // Визуальныей элемент - список/таблица автомобилей
  // Задачи
  //   - Просмотр
  //   - Добавление автомобилей
  //   - Редактирование автомобилей
  //   - Удаление автомобилей
  TCarsController = class(TFrame)
    topPanel: TPanel;
    refreshButton: TButton;
    newButton: TButton;
    editButton: TButton;
    deleteButton: TButton;
    carsDataSource: TDataSource;
    carsDBGrid: TDBGrid;
    carsADOQuery: TADOQuery;

    // Обновление списка отобрадаемых записей
    procedure refreshButtonClick(Sender: TObject);

    // Добавление новой записи
    procedure newButtonClick(Sender: TObject);

    // Редактирование выбранной записи
    procedure editButtonClick(Sender: TObject);

    // Удаление выбранных записей
    procedure deleteButtonClick(Sender: TObject);
  private
    logInstance: ILog;

    // Обновление всех записей
    procedure RefreshAll();

    // Обновление текущей строки
    procedure RefreshCurrent();
  public
    // Подключение СУБД, активизация кнопок и прочего для управления
    procedure ActivateDataView();
  end;

implementation

var
log: ILog;

{$R *.dfm}

procedure TCarsController.ActivateDataView();
begin
  log.println('ActivateDataView');
  carsADOQuery.Active := true;
  dbViewPreparer.prepareGrid(Self.ClassName, carsDBGrid);

  refreshButton.Enabled := true;
  newButton.Enabled := true;
  editButton.Enabled := true;
  deleteButton.Enabled := true;
end;

procedure TCarsController.RefreshCurrent();
begin
  log.println('RefreshCurrent');
  carsADOQuery.Refresh;
  carsDBGrid.Refresh;
end;

procedure TCarsController.RefreshAll();
begin
  log.println('RefreshAll');
  carsADOQuery.Active := false;
  carsADOQuery.Active := true;
  dbViewPreparer.prepareGrid(Self.ClassName, carsDBGrid);
end;



procedure TCarsController.refreshButtonClick(Sender: TObject);
begin
  log.println('refreshButtonClick');
  refreshAll;
end;

procedure TCarsController.newButtonClick(Sender: TObject);
var
  insertDialog : TCarController;
begin
  log.println('newButtonClick');
  insertDialog := TCarController.Create(self);
  FormConfigure(insertDialog);
  try
    if insertDialog.insertDialog(self.carsADOQuery.Connection) then
    begin
      refreshAll;

      extend(carsDBGrid).SelectAndFocus(
        TDataRowValueEqualsPredicate.Create('id', insertDialog.getInsertedId)
      );

      carsDBGrid.SetFocus;
    end;
  finally
    FreeAndNil(insertDialog);
  end;
end;

procedure TCarsController.editButtonClick(Sender: TObject);
var
  updateDialog : TCarController;
  curRow: TStringMap;
begin
  log.println('editButtonClick');
  curRow := TStringMap.Create;
  try
    if extend(carsDBGrid).GetFocusedRow(curRow) then begin
      updateDialog := TCarController.Create(self);
      FormConfigure(updateDialog);
      try
        if updateDialog.updateDialog(
          self.carsADOQuery.Connection,
          curRow.get('id'),
          curRow.get('legal_number'),
          curRow.get('model_id'),
          curRow.get('wear'),
          curRow.get('birth_year'),
          curRow.get('maintenance_s'),
        ) then begin
          refreshCurrent;
        end;
      finally;
        FreeAndNil(updateDialog);
      end;
    end;
  finally
    FreeAndNil(curRow);
  end;
end;

procedure TCarsController.deleteButtonClick(Sender: TObject);
var
  rows: TDBRows;
  rowDelete:  TDBRowsSqlExec;
  query: TADOQuery;
begin
  log.println('deleteButtonClick');
  rows := TDBRows.Create;

  query := TADOQuery.Create(nil);
  query.Connection := carsADOQuery.Connection;
  query.SQL.Text := 'delete from cars where [id] = :ID';

  rowDelete := TDBRowsSqlExec.Create(query);
  rowDelete.Map('id', 'id');
  try
    extend(carsDBGrid)
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

initialization
log := logger('CarsFrame');

end.
