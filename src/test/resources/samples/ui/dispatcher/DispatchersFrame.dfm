object DispatchersController: TDispatchersController
  Left = 0
  Top = 0
  Width = 476
  Height = 240
  TabOrder = 0
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 476
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
      Width = 89
      Height = 25
      Caption = #1056#1077#1076#1072#1082#1090#1080#1088#1086#1074#1072#1090#1100
      Enabled = False
      TabOrder = 2
      OnClick = editButtonClick
    end
    object deleteButton: TButton
      Left = 264
      Top = 8
      Width = 75
      Height = 25
      Caption = #1059#1076#1072#1083#1080#1090#1100
      Enabled = False
      TabOrder = 3
      OnClick = deleteButtonClick
    end
  end
  object dispatchersDBGrid: TDBGrid
    Left = 0
    Top = 41
    Width = 476
    Height = 199
    Align = alClient
    DataSource = dispatchersDataSource
    Options = [dgTitles, dgIndicator, dgColumnResize, dgColLines, dgRowLines, dgTabs, dgConfirmDelete, dgCancelOnExit, dgMultiSelect]
    ReadOnly = True
    TabOrder = 1
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'Tahoma'
    TitleFont.Style = []
  end
  object dispatchersDataSource: TDataSource
    AutoEdit = False
    DataSet = dispatchersADOQuery
    Left = 136
    Top = 72
  end
  object dispatchersADOQuery: TADOQuery
    CursorLocation = clUseServer
    Parameters = <>
    SQL.Strings = (
      
        'select id, name, birth_day, (convert(nvarchar(50), birth_day, 23' +
        ')) as birth_day_s'
      'from dispatchers')
    Left = 104
    Top = 136
  end
end
