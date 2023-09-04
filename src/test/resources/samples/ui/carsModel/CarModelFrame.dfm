object CarModelController: TCarModelController
  Left = 851
  Top = 183
  Width = 319
  Height = 134
  Caption = #1056#1077#1076#1072#1082#1090#1080#1088#1086#1074#1072#1085#1080#1077' '#1084#1086#1076#1077#1083#1080
  Color = clBtnFace
  Constraints.MinHeight = 134
  Constraints.MinWidth = 319
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  DesignSize = (
    303
    95)
  PixelsPerInch = 96
  TextHeight = 13
  object nameEdit: TLabeledEdit
    Left = 8
    Top = 24
    Width = 289
    Height = 21
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 88
    EditLabel.Height = 13
    EditLabel.Caption = #1053#1072#1079#1074#1072#1085#1080#1077' '#1084#1086#1076#1077#1083#1080
    TabOrder = 0
  end
  object doButton: TButton
    Left = 216
    Top = 48
    Width = 75
    Height = 25
    Anchors = [akTop, akRight]
    Caption = #1044#1086#1073#1072#1074#1080#1090#1100
    TabOrder = 1
    OnClick = doButtonClick
  end
  object ADOQueryInsert: TADOQuery
    Parameters = <>
    Left = 8
    Top = 56
  end
  object ADOQueryUpdate: TADOQuery
    Parameters = <
      item
        Name = 'name_param'
        DataType = ftWideString
        Size = -1
        Value = Null
      end
      item
        Name = 'id_param'
        DataType = ftInteger
        Value = Null
      end>
    Prepared = True
    SQL.Strings = (
      
        'update cars_model set [name] = :name_param where [id] = :id_para' +
        'm')
    Left = 72
    Top = 56
  end
end
