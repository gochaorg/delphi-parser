unit CarsModelsFrame;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, Grids, DBGrids, StdCtrls, ExtCtrls, DB, ADODB,

  FormConfig,
  CarModelFrame,
  Logging, Loggers,
  Map, DBRows, DBRowPredicate,
  DBRowsSqlExec,
  DBViewConfig
  ;

type
  // ����������� ������� - ������/������� ������� ����
  // ������
  //   - ��������
  //   - ���������� ������
  //   - �������������� ������
  //   - �������� ������
  TCarsModelsController = class(TFrame)
    topPanel: TPanel;
    refreshButton: TButton;
    newButton: TButton;
    editButton: TButton;
    deleteButton: TButton;
    carModelDBGrid: TDBGrid;
    _DataSource: TDataSource;
    ADOQuery1: TADOQuery;

    // ���������� ������ ������������ �������
    procedure refreshButtonClick(Sender: TObject);
    procedure carModelDBGridTitleClick(Column: TColumn);

    // ���������� ����� ������
    procedure newButtonClick(Sender: TObject);

    // �������������� ��������� ������
    procedure editButtonClick(Sender: TObject);

    // �������� ��������� �������
    procedure deleteButtonClick(Sender: TObject);
  private
    // ���������� ���� �������
    procedure RefreshAll();

    // ���������� ������� ������
    procedure RefreshCurrent();
  public
    // ����������� ����, ����������� ������ � ������� ��� ����������
    procedure ActivateDataView();
  end;

implementation

uses
  DBView;

var
  log: ILog;

{$R *.dfm}

{ TCarsModelsController }

procedure TCarsModelsController.ActivateDataView();
begin
  log.println('ActivateDataView');
  ADOQuery1.Active := true;
  dbViewPreparer.prepareGrid(Self.ClassName, carModelDBGrid);

  refreshButton.Enabled := true;
  newButton.Enabled := true;
  editButton.Enabled := true;
  deleteButton.Enabled := true;
end;

procedure TCarsModelsController.refreshButtonClick(Sender: TObject);
begin
  log.println('refreshButtonClick');
  refreshAll;
end;

procedure TCarsModelsController.carModelDBGridTitleClick(Column: TColumn);
begin
  log.println('carModelDBGridTitleClick');
  ADOQuery1.Active := false;
  ADOQuery1.SQL.Clear();
  ADOQuery1.SQL.Add('select * from cars_model order by name desc');
  ADOQuery1.Active := true;
end;

procedure TCarsModelsController.refreshCurrent();
begin
  log.println('refreshCurrent');
  ADOQuery1.Refresh;
end;

procedure TCarsModelsController.refreshAll();
begin
  log.println('refreshAll');
  ADOQuery1.Active := false;
  ADOQuery1.Active := true;
  dbViewPreparer.prepareGrid(Self.ClassName, carModelDBGrid);
end;

procedure TCarsModelsController.newButtonClick(Sender: TObject);
var
  insertDialog : TCarModelController;
  rows: TDBRows;
begin
  log.println('newButtonClick');
  insertDialog := TCarModelController.Create(self);
  FormConfigure(insertDialog);

  rows := TDBRows.Create;
  try
    if insertDialog.insertDialog( ADOQuery1.Connection ) then begin
      refreshAll;

      extend(carModelDBGrid).SelectAndFocus(
        TDataRowValueEqualsPredicate.Create('id', insertDialog.getInsertedId)
      );

      carModelDBGrid.SetFocus;
    end;
  finally
    FreeAndNil(insertDialog);
    FreeAndNil(rows);
  end;
end;

procedure TCarsModelsController.editButtonClick(Sender: TObject);
var
  updateDialog : TCarModelController;
  curRow: TStringMap;
begin
  log.println('editButtonClick');
  if extend(carModelDBGrid).getRowsCount > 0 then
  begin
    curRow := TStringMap.Create;
    try
      if extend(carModelDBGrid).GetFocusedRow(curRow) then begin
        try
          updateDialog := TCarModelController.Create(self);
          FormConfigure(updateDialog);

        if updateDialog.updateDialog(
            ADOQuery1.Connection, 
            curRow.get('id'), 
            curRow.get('name')
        ) then begin
          refreshCurrent;
        end;

        finally
          FreeAndNil(updateDialog);
        end;      
      end;
    finally
      FreeAndNil(curRow);
    end;
  end;
end;

procedure TCarsModelsController.deleteButtonClick(Sender: TObject);
var
  rows: TDBRows;
  rowDelete:  TDBRowsSqlExec;
  query: TADOQuery;
begin
  log.println('deleteButtonClick');
  rows := TDBRows.Create;

  query := TADOQuery.Create(self);
  query.Connection := ADOQuery1.Connection;
  query.SQL.Text := 'delete from cars_model where [id] = :ID';

  rowDelete := TDBRowsSqlExec.Create(query);
  rowDelete.Map('id', 'id');
  try
    extend(carModelDBGrid)
      .fetchActiveRecord(true)
      .fetchRows(true,false, rows.Add);

    rows.Each(rowDelete.Execute);
    if rowDelete.getErrorsCount > 0 then
      begin
        ShowMessage('� �������� �������� ���������� ������');
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
log := logger('CarsModelsFrame');

end.
