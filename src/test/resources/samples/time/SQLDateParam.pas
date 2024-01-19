unit SQLDateParam;

interface

uses
  Config, MyDate;

// Конвертирует дату, в формат пригодный для SQL
function DateToSQL( date: TMyDate ):variant;

implementation

function DateToSQL( date: TMyDate ):variant;
begin
  if applicationConfigObj.getDateToSqlMethod = 'ToDateTime' then
    result := date.ToDateTime
  else if applicationConfigObj.getDateToSqlMethod = 'ToMSSQLDateTime2' then
    result := date.ToMSSQLDateTime2
  else if applicationConfigObj.getDateToSqlMethod = 'ToString' then
    result := date.ToString(applicationConfigObj.getSqlDateFormat)
  else
    result := date.ToMSSQLDateTime2;
end;

end.
