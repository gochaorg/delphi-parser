unit DBRowPredicate;

interface
  uses Map;

  type
    // �������� ������ ������
    IDataRowPredicate = interface
      // ������� �� ���������� ������
      function test( row: TStringMap ):boolean;
    end;

    // �������� �� ��������� ���� ������ ������
    TDataRowValueEqualsPredicate = class(TInterfacedObject,IDataRowPredicate)
      private
        key:string;
        expectedValue:variant;
      public
        // �����������
        // ��������
        //   key - ��� �������
        //   value - ��������
        constructor Create(key:string; value:variant);
        destructor Destroy(); override;
        function test( row: TStringMap ):boolean;
    end;

implementation

{ TRowValueEqualsPredicate }

constructor TDataRowValueEqualsPredicate.Create(key:string; value:variant);
begin
  inherited Create;
  self.key := key;
  self.expectedValue := value;
end;

destructor TDataRowValueEqualsPredicate.Destroy;
begin
  inherited Destroy;
end;

function TDataRowValueEqualsPredicate.test(row: TStringMap): boolean;
begin
  result := false;
  if row.exists(self.key) then
  begin
    if row.get(self.key) = self.expectedValue then begin
      result := true;
    end;
  end;
end;

end.
