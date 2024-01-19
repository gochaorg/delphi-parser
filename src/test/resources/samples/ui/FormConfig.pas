unit FormConfig;

// ��������� ������������ ������������ ����

interface

uses
  Forms, Controls, IniFiles,
  Config;

// ������������ ����
procedure FormConfigure( form: TForm );

implementation

// ����������� ���� � ������ ������
procedure MoveToCenter( form: TForm );
var
  xCenter,yCenter:Integer;
begin
  xCenter := Screen.WorkAreaLeft + Round(Screen.WorkAreaWidth / 2);
  yCenter := Screen.WorkAreaTop + Round(Screen.WorkAreaHeight / 2);

  form.Left := xCenter - Round(form.Width / 2);
  form.Top  := yCenter - Round(form.Height / 2);
end;

procedure FormConfigure( form: TForm );
begin
  MoveToCenter(form);
end;

end.
