object CarsController: TCarsController
  Left = 0
  Top = 0
  Width = 487
  Height = 281
  TabOrder = 0
  object topPanel: TPanel
    Left = 0
    Top = 0
    Width = 487
    Height = 41
    Align = alTop
    TabOrder = 0
    object refreshButton: TButton
      Left = 8
      Top = 8
      Width = 75
      Height = 25
      Caption = #1054#1073#1085#1086#1074#1080#1090#1100
      Enabled = False
      TabOrder = 0
      OnClick = refreshButtonClick
    end
    object newButton: TButton
      Left = 88
      Top = 8
      Width = 75
      Height = 25
      Caption = #1044#1086#1073#1072#1074#1080#1090#1100
      Enabled = False
      TabOrder = 1
      OnClick = newButtonClick
    end
    object editButton: TButton
      Left = 168
      Top = 8
      Width = 97
      Height = 25
      Caption = #1056#1077#1076#1072#1082#1090#1080#1088#1086#1074#1072#1090#1100
      Enabled = False
      TabOrder = 2
      OnClick = editButtonClick
    end
    object deleteButton: TButton
      Left = 272
      Top = 8
      Width = 75
      Height = 25
      Caption = #1059#1076#1072#1083#1080#1090#1100
      Enabled = False
      TabOrder = 3
      OnClick = deleteButtonClick
    end
  end
  object carsDBGrid: TDBGrid
    Left = 0
    Top = 41
    Width = 487
    Height = 240
    Align = alClient
    DataSource = carsDataSource
    Options = [dgTitles, dgIndicator, dgColumnResize, dgColLines, dgRowLines, dgTabs, dgConfirmDelete, dgCancelOnExit, dgMultiSelect]
    ReadOnly = True
    TabOrder = 1
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'Tahoma'
    TitleFont.Style = []
  end
  object carsDataSource: TDataSource
    AutoEdit = False
    DataSet = carsADOQuery
    Left = 304
    Top = 104
  end
  object carsADOQuery: TADOQuery
    CursorLocation = clUseServer
    Parameters = <>
    SQL.Strings = (
      'select '
      #9'c.id, '
      #9'legal_number, '
      #9'c.model as model_id, '
      #9'cm.name as model_name, '
      #9'wear, '
      #9'birth_year, '
      #9'maintenance,'
      #9'( convert(nvarchar(50), c.maintenance, 23) ) as maintenance_s'
      'from cars c'
      'left join cars_model cm on (cm.id = c.model)')
    Left = 112
    Top = 96
  end
end
