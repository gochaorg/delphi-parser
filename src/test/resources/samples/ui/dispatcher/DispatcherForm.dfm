object DispatcherController: TDispatcherController
  Left = 1414
  Top = 326
  Width = 384
  Height = 208
  Caption = 'DispatcherController'
  Color = clBtnFace
  Constraints.MinHeight = 208
  Constraints.MinWidth = 384
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnHide = FormHide
  OnShow = FormShow
  DesignSize = (
    368
    169)
  PixelsPerInch = 96
  TextHeight = 13
  object errLabel: TLabel
    Left = 8
    Top = 104
    Width = 39
    Height = 13
    Caption = 'errLabel'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clMaroon
    Font.Height = -11
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
  end
  object nameEdit: TLabeledEdit
    Left = 8
    Top = 24
    Width = 345
    Height = 21
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 53
    EditLabel.Height = 13
    EditLabel.Caption = #1048#1084#1103' ('#1060#1048#1054')'
    TabOrder = 0
  end
  object birthDayEdit: TLabeledEdit
    Left = 8
    Top = 72
    Width = 345
    Height = 21
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 143
    EditLabel.Height = 13
    EditLabel.Caption = #1044#1072#1090#1072' '#1088#1086#1078#1076#1077#1085#1080#1103' yyyy-mm-dd'
    TabOrder = 1
  end
  object okButton: TButton
    Left = 280
    Top = 104
    Width = 75
    Height = 25
    Anchors = [akTop, akRight]
    Caption = 'okButton'
    TabOrder = 2
    OnClick = okButtonClick
  end
end
