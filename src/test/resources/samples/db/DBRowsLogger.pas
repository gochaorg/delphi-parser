unit DBRowsLogger;

interface

  uses
    Logging, Map;

  type

  ///////////////////////////////////////
  // Запись в лог выборки
  IDBRowsLogger = interface
    procedure Add(row:TStringMap);
    procedure Addi(row:IStringMap);
  end;

  TDBRowsLogger = class(TInterfacedObject,IDBRowsLogger)
    private
      logger: ILog;
    public
      constructor Create( logger:ILog );
      destructor Destroy; override;
      procedure Add(row:TStringMap); virtual;
      procedure Addi(row:IStringMap); virtual;
  end;

implementation

{ TDBRowsLogger }

constructor TDBRowsLogger.Create(logger: ILog);
begin
  inherited Create;
  self.logger := logger;
end;

destructor TDBRowsLogger.Destroy;
begin
  self.logger := nil;
  inherited Destroy;
end;

procedure TDBRowsLogger.Add(row: TStringMap);
begin
  self.logger.print('row: ');
  self.logger.println( row.toString );
end;

procedure TDBRowsLogger.Addi(row: IStringMap);
begin
  self.logger.print('row: ');
  self.logger.println( row.toString );
end;

end.
