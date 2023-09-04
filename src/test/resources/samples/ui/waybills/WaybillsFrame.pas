unit WaybillsFrame;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, Grids, DBGrids, ExtCtrls, StdCtrls, DB, ADODB, ComObj,

  DBRows, DBRowPredicate, DBView, Map, DBRowsSqlExec,
  DBViewConfig, 

  WaybillForm, WaybillSQLView, FormConfig,
  Loggers, Logging
  ;

type
  // Контроллер управления путевыми листами
  // Основнфе функции
  //   Просмотр
  //   Добавление
  //   Редактирование
  //   Удаление
  TWaybillsController = class(TFrame)
    Panel1: TPanel;
    waybillsDBGrid: TDBGrid;
    refreshButton: TButton;
    newButton: TButton;
    editButton: TButton;
    deleteButton: TButton;
    waybillsDataSource: TDataSource;
    waybillsADOQuery: TADOQuery;
    showHistoryCheckBox: TCheckBox;
    findEdit: TEdit;
    findButton: TButton;
    findPanel: TPanel;
    crudPanel: TPanel;

    // Добавление новой записи
    procedure newButtonClick(Sender: TObject);

    // Обновление списка отобрадаемых записей
    procedure refreshButtonClick(Sender: TObject);

    // Редактирование выбранной записи
    procedure editButtonClick(Sender: TObject);

    // Удаление выбранных записей
    procedure deleteButtonClick(Sender: TObject);

    // Вкл/Выкл отображение истории
    procedure showHistoryCheckBoxClick(Sender: TObject);

    // Отрисовка фона ячейки
    procedure waybillsDBGridDrawColumnCell(Sender: TObject;
      const Rect: TRect; DataCol: Integer; Column: TColumn;
      State: TGridDrawState);

    // Указание фильтра искомых данных
    procedure findButtonClick(Sender: TObject);

    // Сортировка по указанной колнке
    procedure waybillsDBGridTitleClick(Column: TColumn);
  private
    // Сколбко раз была вызвана активация
    activateDataViewCalledCount: Integer;

    queryBuilderValue: IWaybillsQueryBuilder;
    function queryBuilder: IWaybillsQueryBuilder;

    // Активно или нет
    // т.е. есть подключение к СУБД
    function isActivated: boolean;

    // Пересоздание и выполнение запроса SELECT
    procedure RebuildQuery();
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
// логи
log : ILog;

{$R *.dfm}

procedure TWaybillsController.ActivateDataView();
begin
  log.println('ActivateDataView');

  waybillsADOQuery.Active := true;
  self.RebuildQuery;

  dbViewPreparer.prepareGrid(Self.ClassName, waybillsDBGrid);

  refreshButton.Enabled := true;
  newButton.Enabled := true;
  editButton.Enabled := true;
  deleteButton.Enabled := true;
  showHistoryCheckBox.Enabled := true;
  findEdit.Enabled := true;
  findButton.Enabled := true;

  activateDataViewCalledCount := activateDataViewCalledCount + 1;
end;

procedure TWaybillsController.RefreshCurrent();
begin
  try
    log.println('RefreshCurrent');
    waybillsADOQuery.Refresh;
  except
    on e:EOleException do begin
      log.println('! RefreshCurrent error: '+e.Message);
      ShowMessage('Ошибка чтения данных: '+e.Message);
    end;
  end;
end;

procedure TWaybillsController.RefreshAll();
begin
  log.println('RefreshAll');
  waybillsADOQuery.Active := false;
  try
    waybillsADOQuery.Active := true;
  except
    on e:EOleException do begin
      log.println('! RefreshAll error: '+e.Message);
      ShowMessage('Ошибка чтения данных: '+e.Message);
    end;
  end;
  dbViewPreparer.prepareGrid(Self.ClassName, waybillsDBGrid);
end;

procedure TWaybillsController.newButtonClick(Sender: TObject);
var
  insertDialog : TWaybillController;
begin
  log.println('newButtonClick');
  insertDialog := TWaybillController.Create(self);
  FormConfigure(insertDialog);
  try
    if insertDialog.InsertDialog(waybillsADOQuery.Connection) then begin
      RefreshAll;

      extend(waybillsDBGrid).SelectAndFocus(
        TDataRowValueEqualsPredicate.Create('id', insertDialog.getInsertedId)
      );

      waybillsDBGrid.SetFocus;
    end
  finally
    FreeAndNil(insertDialog);
  end;
end;

procedure TWaybillsController.refreshButtonClick(Sender: TObject);
begin
  log.println('refreshButtonClick');
  RefreshAll;
end;

procedure TWaybillsController.editButtonClick(Sender: TObject);
var
  updateDialog : TWaybillController;
  curRow: TStringMap;
begin
  log.println('editButtonClick');
  curRow := TStringMap.Create;
  try
    if extend(waybillsDBGrid).GetFocusedRow(curRow) then begin
      log.println('curRow '+curRow.toString);
      if curRow.get('state') = 'actual' then begin
        updateDialog := TWaybillController.Create(self);
        FormConfigure(updateDialog);
        try
          if updateDialog.updateDialog(
            self.waybillsADOQuery.Connection,
            curRow.get('id'),
            curRow.get('income_date_s'),
            curRow.get('outcome_date_s'),
            curRow.get('driver_id'),
            curRow.get('driver_name'),
            curRow.get('dispatcher_id'),
            curRow.get('dispatcher_name'),
            curRow.get('car_id'),
            curRow.get('car_model_id'),
            curRow.get('car_model_name'),
            curRow.get('car_legal_number'),
            curRow.get('car_total_wear'),
            curRow.get('wear'),
            curRow.get('fuel_cons')
          ) then begin
            refreshCurrent;
            waybillsDBGrid.SetFocus;
          end;
        finally;
          FreeAndNil(updateDialog);
        end;
      end;
    end;
  finally
    FreeAndNil(curRow);
  end;
end;

procedure TWaybillsController.deleteButtonClick(Sender: TObject);
var
  rows: TDBRows;
  rowDelete:  TDBRowsSqlExec;
  query: TADOQuery;
begin
  log.println('deleteButtonClick');
  rows := TDBRows.Create;

  query := TADOQuery.Create(nil);
  query.Connection := waybillsADOQuery.Connection;
  query.SQL.Text := 'delete from waybills where [id] = :ID';

  rowDelete := TDBRowsSqlExec.Create(query);
  rowDelete.Map('id', 'id');
  try
    extend(waybillsDBGrid)
      .fetchActiveRecord(true)
      .fetchRows(true,false, rows.Add);
    log.println('fetched '+IntToStr(rows.GetCount)+' rows');

    rows.Retain(TDataRowValueEqualsPredicate.Create('state', 'actual'));
    log.println('Retain '+IntToStr(rows.GetCount)+' rows');

    rows.Each(rowDelete.Execute);
    if rowDelete.getErrorsCount > 0 then
      begin
        ShowMessage('В процессе удаления обнаружены ошибки');
      end
    else
      begin
        refreshAll;
        waybillsDBGrid.SetFocus;
      end;
  finally
    FreeAndNil(query);
    FreeAndNil(rows);
    FreeAndNil(rowDelete);
  end;
end;

procedure TWaybillsController.showHistoryCheckBoxClick(Sender: TObject);
begin
  log.println('showHistoryCheckBoxClick');
  self.newButton.Enabled := not self.showHistoryCheckBox.Checked;
  self.editButton.Enabled := not self.showHistoryCheckBox.Checked;
  self.deleteButton.Enabled := not self.showHistoryCheckBox.Checked;

  self.rebuildQuery;
end;

procedure TWaybillsController.RebuildQuery;
begin
  log.println('RebuildQuery');
  self.queryBuilder.history := showHistoryCheckBox.Checked;

  if length(trim(findEdit.Text))>0 then begin
    self.queryBuilder.whereExpresion :=
      TWhereSearchTextLikeExpression.Create('%'+findEdit.Text+'%');
  end else begin
    self.queryBuilder.resetWhereExpression;
  end;
  
  if self.isActivated then begin
    queryBuilder.build.apply(waybillsADOQuery);
    dbViewPreparer.prepareGrid(Self.ClassName, waybillsDBGrid);
  end;
end;

function TWaybillsController.isActivated: boolean;
begin
  result := activateDataViewCalledCount > 0;
end;

function TWaybillsController.queryBuilder: IWaybillsQueryBuilder;
begin
  if assigned(self.queryBuilderValue) then begin
    log.println('queryBuilder get cahced');
    result := self.queryBuilderValue;
  end else begin
    log.println('queryBuilder create');
    self.queryBuilderValue := TWaybillsQueryBuilder.Create;
    result := self.queryBuilderValue;
  end;
end;

procedure TWaybillsController.waybillsDBGridDrawColumnCell(Sender: TObject;
  const Rect: TRect; DataCol: Integer; Column: TColumn;
  State: TGridDrawState);
var
  isActual : boolean;
  row: TStringMap;

  bgHistColor: TColor;
  bgHistSelectedColor: TColor;
  bgHistFocusedColor: TColor;

  fgHistColor: TColor;
  fgHistSelectedColor: TColor;
  fgHistFocusedColor: TColor;

  bgColor: TColor;
  fgColor: TColor;
begin
  fgColor := TColor($00000000);
  bgColor := TColor($00ffFFff);

  bgHistColor := TColor($00bbFFbb);
  fgHistColor := TColor($00000000);

  bgHistSelectedColor := TColor($0088FF88);
  fgHistSelectedColor := TColor($00000000);

  bgHistFocusedColor := TColor($0000bb00);
  fgHistFocusedColor := TColor($00ffFFff);

  row := TStringMap.Create;
  try
    if extend(waybillsDBGrid).GetCurrentRow(row) then begin
      if row.get('state') = 'hist'
      then begin
        fgColor := fgHistColor;
        bgColor := bgHistColor;

        if gdSelected in state then begin
          bgColor := bgHistSelectedColor;
          fgColor := fgHistSelectedColor;
        end;

        if gdFocused in state then begin
          bgColor := bgHistFocusedColor;
          fgColor := fgHistFocusedColor;
        end;

        waybillsDBGrid.Canvas.Brush.Color := bgColor;
        waybillsDBGrid.Canvas.Font.Color := fgColor;
        waybillsDBGrid.DefaultDrawColumnCell(Rect, DataCol, Column, State);
      end;
    end;
  finally
    FreeAndNil(row);
  end;
end;

procedure TWaybillsController.findButtonClick(Sender: TObject);
begin
  log.println('findButtonClick');
  self.RebuildQuery;
end;

procedure TWaybillsController.waybillsDBGridTitleClick(Column: TColumn);
begin
  log.println('waybillsDBGridTitleClick');
  self.queryBuilder.toggleOrder(Column.FieldName);
  self.RebuildQuery;
end;

initialization
log := logger('WaybillsFrame');

end.
