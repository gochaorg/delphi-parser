unit some;
interface
const
{$IFDEF Cond1}
  {$IF Cond3}
  {$ELSE}  
  {$ENDIF}
{$ELSEIF Cond2}
  {$IF Cond4}
  {$ELSE}  
  {$ENDIF}
{$ELSE}
  {$IF Cond5}
  {$ELSE}  
  {$ENDIF}
{$ENDIF}
implementation
end.