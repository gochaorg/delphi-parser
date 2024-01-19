object CarController: TCarController
  Left = 1444
  Top = 186
  Width = 341
  Height = 443
  Caption = 'CarController'
  Color = clBtnFace
  Constraints.MinHeight = 443
  Constraints.MinWidth = 341
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnDestroy = FormDestroy
  OnShow = FormShow
  DesignSize = (
    325
    404)
  PixelsPerInch = 96
  TextHeight = 13
  object leagalNumLabel: TLabel
    Left = 16
    Top = 56
    Width = 39
    Height = 13
    Caption = #1052#1086#1076#1077#1083#1100
  end
  object errLabel: TLabel
    Left = 16
    Top = 376
    Width = 39
    Height = 13
    Anchors = [akLeft, akBottom]
    Caption = 'errLabel'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clMaroon
    Font.Height = -11
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
  end
  object legalNumberEdit: TLabeledEdit
    Left = 16
    Top = 24
    Width = 297
    Height = 21
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 50
    EditLabel.Height = 13
    EditLabel.Caption = #1043#1086#1089' '#1085#1086#1084#1077#1088
    TabOrder = 0
    Text = #1043#1086#1089' '#1053#1086#1084#1077#1088
  end
  object modelListBox: TListBox
    Left = 16
    Top = 72
    Width = 297
    Height = 145
    Anchors = [akLeft, akTop, akRight, akBottom]
    ItemHeight = 13
    TabOrder = 1
  end
  object wearEdit: TLabeledEdit
    Left = 16
    Top = 240
    Width = 297
    Height = 21
    Anchors = [akLeft, akRight, akBottom]
    EditLabel.Width = 36
    EditLabel.Height = 13
    EditLabel.Caption = #1055#1088#1086#1073#1077#1075
    TabOrder = 2
    Text = '0'
  end
  object birthYearEdit: TLabeledEdit
    Left = 16
    Top = 288
    Width = 297
    Height = 21
    Anchors = [akLeft, akRight, akBottom]
    EditLabel.Width = 100
    EditLabel.Height = 13
    EditLabel.Caption = #1043#1086#1076' '#1074#1099#1087#1091#1089#1082#1072' (yyyy)'
    TabOrder = 3
    Text = '2020'
  end
  object maintainceEdit: TLabeledEdit
    Left = 16
    Top = 336
    Width = 297
    Height = 21
    Anchors = [akLeft, akRight, akBottom]
    EditLabel.Width = 186
    EditLabel.Height = 13
    EditLabel.Caption = #1044#1072#1090#1072' '#1087#1088#1086#1093#1086#1078#1076#1077#1085#1080#1103' '#1058#1054' (yyyy-mm-dd)'
    TabOrder = 4
    Text = '1956-01-25'
  end
  object okButton: TButton
    Left = 240
    Top = 368
    Width = 75
    Height = 25
    Anchors = [akRight, akBottom]
    Caption = 'okButton'
    TabOrder = 5
    OnClick = okButtonClick
  end
  object insertADOQuery: TADOQuery
    Parameters = <
      item
        Name = 'legal_number'
        Size = -1
        Value = Null
      end
      item
        Name = 'model'
        Size = -1
        Value = Null
      end
      item
        Name = 'wear'
        Size = -1
        Value = Null
      end
      item
        Name = 'bearth_year'
        Size = -1
        Value = Null
      end
      item
        Name = 'maintenance'
        Size = -1
        Value = Null
      end>
    SQL.Strings = (
      
        'insert cars (legal_number, model, wear, bearth_year, maintenance' +
        ')'
      'values ('
      #9':legal_number, :model, :wear, :bearth_year, '
      #9'convert( datetime2, :maintenance, 23 )'
      ')')
    Left = 40
    Top = 112
  end
  object updateADOQuery: TADOQuery
    Parameters = <>
    Left = 152
    Top = 120
  end
  object carsModelADOQuery: TADOQuery
    Parameters = <>
    SQL.Strings = (
      'select id, name from cars_model order by name')
    Left = 248
    Top = 120
  end
end
