function factorial(a:integer):integer;
begin
 if a < 2 then 
  begin factorial := 1; end
 else 
  begin factorial := a*factorial(a-1); end;
end;

begin
 writeln(factorial(7));
end.
