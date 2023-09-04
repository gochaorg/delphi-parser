unit IntegerList;

interface

uses
  SysUtils,
  Classes;

type

// Ошибка пр работе со списком
EIntegerList = class(Exception);

// Ощибка - выход за пределы списка
EOutOfIntegerList = class(EIntegerList);

// Список чисел
IIntegerList = interface
  // Возвращает true если список пуст
  function isEmpty: boolean;
  property Empty:Boolean read isEmpty;

  // Возвращает кол-во элементов
  function GetCount: Integer;
  property Count:Integer read GetCount;

  // Возвращает элемент по его индексу (от 0 и более)
  function Get(index:Integer): Integer;
  property Items[index:Integer]:Integer read Get;

  // Возвращает индекс элемента
  // или -1 если не найден
  function IndexOf(const value:Integer): Integer;

  // Добавляет элемент в список
  procedure Add(const value:Integer);

  // Удаляет все элементы из списка
  procedure Clear;

  // Возвращает текстовое представление
  // Аргументы
  //  delimiter - разделитель между значениями
  function toString(delimiter:string):string;
end;

TIntegerList = class(TInterfacedObject,IIntegerList)
  private
    list: TList;
  public
    constructor Create;
    destructor Destroy; override;

    function isEmpty: boolean; virtual;
    property Empty:Boolean read isEmpty;

    function GetCount: Integer; virtual;
    property Count:Integer read GetCount;

    function Get(index:Integer): Integer;  virtual;
    property Items[index:Integer]:Integer read Get;

    function IndexOf(const value:Integer): Integer; virtual;

    procedure Add(const value:Integer); virtual;
    procedure Clear; virtual;

    function toString(delimiter:string):string; virtual;
  end;

implementation

type

TItem = class
  public
    value: Integer;
    constructor Create(value: Integer);
end;

{ TIntegerList }

constructor TIntegerList.Create;
begin
  inherited Create;
  self.list := TList.Create;
end;

destructor TIntegerList.Destroy;
begin
  self.Clear;
  FreeAndNil(self.list);
  inherited Destroy;
end;


procedure TIntegerList.Add(const value: Integer);
begin
  self.list.Add(TItem.Create(value));
end;

procedure TIntegerList.Clear;
var
  i:Integer;
  itm: TItem;
begin
  for i:=self.list.Count-1 downto 0 do begin
    itm := self.list[i];
    FreeAndNil(itm);
    list.Delete(i);
  end;
end;

function TIntegerList.Get(index: Integer): Integer;
var
  itm: TItem;
begin
  if index<0 then raise EOutOfIntegerList.Create('index less zero');
  if index>=list.Count then EOutOfIntegerList.Create('index more or equals list.Count');
  itm := self.list[index];
  result := itm.value;
end;

function TIntegerList.GetCount: Integer;
begin
  result := list.Count;
end;

function TIntegerList.IndexOf(const value: Integer): Integer;
var
  i: Integer;
  itm: TItem;
begin
  result := -1;
  for i:=0 to list.Count-1 do begin
    itm := self.list[i];
    if itm.value = value then begin
      result := i;
      break;
    end;
  end;
end;

function TIntegerList.isEmpty: boolean;
begin
  result := self.list.Count > 0;
end;

function TIntegerList.toString(delimiter: string): string;
var
  i:Integer;
begin
  result := '';
  for i:=0 to list.Count-1 do begin
    if i>0 then result := result + delimiter;
    result := result + IntToStr(self.Get(i));
  end;
end;

{ TItem }

constructor TItem.Create(value: Integer);
begin
  self.value := value;
end;

end.
