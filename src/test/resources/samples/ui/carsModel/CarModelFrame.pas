unit CarModelFrame;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls, DB, ADODB, ComObj,

  Loggers, Logging;

type
  // Режим InsertMode / UpdateMode
  TMode = (InsertMode, UpdateMode);

  // Диалог добавления/обновления модели
  TCarModelController = class(TForm)
    nameEdit: TLabeledEdit;
    doButton: TButton;
    ADOQueryInsert: TADOQuery;
    ADOQueryUpdate: TADOQuery;

    // Завершение редактирования
    procedure doButtonClick(Sender: TObject);
  private
    // Режим InsertMode / UpdateMode
    mode: TMode;

    // содинение с СУБД
    // не владет ссылкой
    // сылка доступна только на момент пока окно открыто
    connection: TADOConnection;

    // ID добавленной записи в СУБД
    insertedId: Integer;

    // ID обновляемой записи
    updatingId: Integer;

    // добавление успешно выполнено
    insertSuccessfully: Boolean;

    // обновление успешно выполнено
    updateSuccessfully: Boolean;

    // Добавлене записи
    procedure insertData();
    
    // Редактирование записи
    procedure updateData();
  public
    // Открыть диалог для добавления
    // Аргументы
    //   connection - соединение с СУБД
    // Возвращает
    //   true - успешно добавлена запись
    //   false - не добавлена
    function insertDialog(connection: TADOConnection): Boolean;

    // Возвращает id добавленной записи
    function getInsertedId(): Integer;

    // Открыть диалог для обновления
    // Возвращает
    //   true - успено обновлена запись
    //   false - ошибка
    function updateDialog(connection: TADOConnection; id: Integer; name: WideString): Boolean;
  end;

var
  CarModelController: TCarModelController;

implementation

var
  log: ILog;

{$R *.dfm}

procedure TCarModelController.doButtonClick(Sender: TObject);
begin
  if mode = InsertMode
  then
    begin
      insertData();
    end
  else
    begin
      updateData();
    end;
end;

// Добавление данных

procedure TCarModelController.insertData;
var
  insertedId : Integer;
begin
  try
    ADOQueryInsert.Close;
    ADOQueryInsert.Connection := connection;
    ADOQueryInsert.SQL.Clear;
    ADOQueryInsert.SQL.Add('insert into cars_model (name) values (:NAME);');
    ADOQueryInsert.SQL.Add('select @@IDENTITY as _id');

    ADOQueryInsert.Parameters.Clear;
    ADOQueryInsert.Parameters.CreateParameter('NAME', ftWideString, pdInput, 250, 'init');
    ADOQueryInsert.Parameters.ParamByName('NAME').Value := nameEdit.Text;

    ADOQueryInsert.Prepared := true;
    ADOQueryInsert.Open;
    while not ADOQueryInsert.Eof do begin
      insertedId := ADOQueryInsert.FieldByName('_id').AsInteger;
      ADOQueryInsert.Next;
    end;

    insertSuccessfully := true;
    Close;
  finally
    ADOQueryInsert.Close;
    ADOQueryInsert.Parameters.Clear;
    ADOQueryInsert.Connection := nil;
  end;
end;

function TCarModelController.insertDialog(connection: TADOConnection): Boolean;
begin
  result := false;

  self.mode := InsertMode;

  self.connection := connection;
  self.doButton.Caption := 'Добавить';
  self.Caption := 'Добавить модель';
  self.insertSuccessfully := false;

  try
    ShowModal();
    result := self.insertSuccessfully;
  finally
    self.connection := nil;
  end;
end;

function TCarModelController.getInsertedId: Integer;
begin
  result := insertedId;
end;

// Обновление данных

procedure TCarModelController.updateData;
begin
  ADOQueryUpdate.Connection := connection;
  try
    try
      ADOQueryUpdate.Parameters.ParamByName('name_param').Value := nameEdit.Text;
      ADOQueryUpdate.Parameters.ParamByName('id_param').Value := updatingId;
      ADOQueryUpdate.ExecSQL;

      updateSuccessfully := true;
      Close;
    except
      on e:EOleException do
        ShowMessage('Ошибка обновления: '+IntToStr(e.ErrorCode)+' '+e.Message);
    end;    
  finally
    ADOQueryUpdate.Close;
    ADOQueryUpdate.Parameters.Clear;
    ADOQueryUpdate.Connection := nil;
  end;
end;

function TCarModelController.updateDialog(
  connection: TADOConnection;
  id: Integer;
  name: WideString
): Boolean;
begin
  self.mode := UpdateMode;

  self.connection := connection;
  self.doButton.Caption := 'Обновить';
  self.Caption := 'Обновить модель #'+intToStr(id);
  self.updatingId := id;
  self.nameEdit.Text := name;
  self.updateSuccessfully := false;
  result := false;

  try
    ShowModal();
    result := self.updateSuccessfully;
  finally
    self.connection := nil;
  end;
end;

initialization
log := logger('CarModelFrame');

end.
