unit MyDateTime;

// чет не хочет нормально работать mssql 2016 ado
// c TDateTime в качестве параметра для datetime2

interface

{type

TMyDateTime = class
  private
    yearValue: Integer;
    monthValue: Integer;
    dateValue: Integer;

    hourValue: Integer;
    minuteValue: Integer;
    secondValue: Integer;
  public

    property year:Integer read yearValue;
    property month:Integer read monthValue;
    property date:Integer read dateValue;
    property hour:Integer read hourValue;
    property minute:Integer read minuteValue;
    property second:Integer read secondValue;
end;

TMyDateField = (
  Year2,
  Year4,
  Month1,
  Month2,
  Date
)

TMyDateFormat = class
  private
    minLength:Integer;
    maxLength:Integer;


end;
}

implementation

function isLeapYear(year:Integer):boolean;
begin
  result := false;
  if (year mod 400) = 0 then result := true
  else if (year mod 100) = 0 then result := false
  else if (year mod 4) = 0 then result := true;
end;

function getMonthLen(year,mon:Integer):Integer;
begin
  result := 0;
  if mon = 1 then result := 31
  else if (mon = 2) and (isLeapYear(year)) then result := 29
  else if (mon = 2) and (not isLeapYear(year)) then result := 28
  else if mon = 3  then result := 31
  else if mon = 4  then result := 30
  else if mon = 5  then result := 31
  else if mon = 6  then result := 30
  else if mon = 7  then result := 31
  else if mon = 8  then result := 31
  else if mon = 9  then result := 30
  else if mon = 10 then result := 31
  else if mon = 11 then result := 30
  else if mon = 12 then result := 31;
end;


end.
