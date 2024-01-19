unit Validation;

interface

uses
  SysUtils,
  Classes;

type

// Результат валидации (входных данных)
IDataValidation = interface
  // Возвращает есть или нет ошибки валидации
  //  true - нет ошибок
  //  false - есть ошибки
  function isOk(): boolean;

  // Возвращает кол-во ошибок валидации
  function getErrorCount: Integer;

  // Возвращает ошибку валидации
  function getError( index: Integer ):WideString;

  // Возвращает список ошибок разделенных \r\n или пустую строку
  function getMessage(): WideString;
end;

// Мутабельная коллекция
IDataValidationMut = interface(IDataValidation)
  // Добавляет ошибку
  procedure addError( msg: WideString );
end;

TDataValidation = class(TInterfacedObject,IDataValidationMut,IDataValidation)
  private
    errors: TStringList;
  public
    constructor Create;
    destructor Destroy; override;

    procedure addError( msg: WideString );

    function isOk(): boolean;

    function getErrorCount: Integer;
    function getError( index: Integer ):WideString;
    function getMessage(): WideString;
end;

implementation

{ TDataValidation }

constructor TDataValidation.Create;
begin
  inherited Create;
  self.errors := TStringList.Create;
end;

destructor TDataValidation.Destroy;
begin
  FreeAndNil(self.errors);
  inherited Destroy;
end;

procedure TDataValidation.addError(msg: WideString);
begin
  self.errors.Add(msg);
end;

function TDataValidation.getError(index: Integer): WideString;
begin
  result := '';
  if (index>=0) and (index<self.errors.Count) then
  begin
    result := self.errors[index];
  end;
end;

function TDataValidation.getErrorCount: Integer;
begin
  result := self.errors.Count;
end;

function TDataValidation.getMessage: WideString;
var
  i: Integer;
begin
  result := '';
  if self.errors.Count > 0 then
    begin
      for i:=0 to self.errors.Count-1 do begin
        if i>0 then begin
          result := result + #13#10;
        end;
        result := result + IntToStr(i+1) + ': ' + self.errors.Strings[i];
      end;
    end;
end;

function TDataValidation.isOk: boolean;
begin
  result := self.errors.Count = 0;
end;

end.
