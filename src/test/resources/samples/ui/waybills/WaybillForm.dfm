object WaybillController: TWaybillController
  Left = 638
  Top = 159
  Width = 456
  Height = 596
  Caption = 'WaybillController'
  Color = clBtnFace
  Constraints.MinHeight = 596
  Constraints.MinWidth = 456
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnHide = FormHide
  OnShow = FormShow
  DesignSize = (
    440
    557)
  PixelsPerInch = 96
  TextHeight = 13
  object errLabel: TLabel
    Left = 8
    Top = 496
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
  object outcomeDatetimeEdit: TLabeledEdit
    Left = 8
    Top = 24
    Width = 201
    Height = 21
    EditLabel.Width = 193
    EditLabel.Height = 13
    EditLabel.Caption = #1042#1088#1077#1084#1103' '#1074#1099#1077#1079#1076#1072' (yyyy-MM-dd HH:mm:ss)'
    TabOrder = 0
  end
  object incomeDatetimeEdit: TLabeledEdit
    Left = 216
    Top = 24
    Width = 209
    Height = 21
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 101
    EditLabel.Height = 13
    EditLabel.Caption = #1042#1088#1077#1084#1103' '#1074#1086#1079#1074#1088#1072#1097#1077#1085#1080#1103
    TabOrder = 1
  end
  object Panel1: TPanel
    Left = 8
    Top = 48
    Width = 417
    Height = 233
    Anchors = [akLeft, akTop, akRight]
    TabOrder = 2
    object Splitter1: TSplitter
      Left = 201
      Top = 1
      Height = 231
    end
    object dispatcherGroupBox: TGroupBox
      Left = 1
      Top = 1
      Width = 200
      Height = 231
      Align = alLeft
      Caption = #1044#1080#1089#1087#1077#1090#1095#1077#1088
      TabOrder = 0
      object dispatchersListBox: TListBox
        Left = 2
        Top = 40
        Width = 196
        Height = 189
        Align = alClient
        ItemHeight = 13
        TabOrder = 0
      end
      object dispatcherPanel: TPanel
        Left = 2
        Top = 15
        Width = 196
        Height = 25
        Align = alTop
        BevelOuter = bvNone
        TabOrder = 1
        DesignSize = (
          196
          25)
        object dispatcherFindButton: TButton
          Left = 130
          Top = 1
          Width = 61
          Height = 21
          Anchors = [akTop, akRight]
          Caption = #1053#1072#1081#1090#1080
          TabOrder = 0
          OnClick = dispatcherFindButtonClick
        end
        object dispatcherEdit: TEdit
          Left = 4
          Top = 1
          Width = 123
          Height = 21
          Anchors = [akLeft, akTop, akRight]
          TabOrder = 1
          Text = '%'
        end
      end
    end
    object driverGroupBox: TGroupBox
      Left = 204
      Top = 1
      Width = 212
      Height = 231
      Align = alClient
      Caption = #1042#1086#1076#1080#1090#1077#1083#1100
      TabOrder = 1
      object driversListBox: TListBox
        Left = 2
        Top = 40
        Width = 208
        Height = 189
        Align = alClient
        ItemHeight = 13
        TabOrder = 0
      end
      object driverFindPanel: TPanel
        Left = 2
        Top = 15
        Width = 208
        Height = 25
        Align = alTop
        BevelOuter = bvNone
        TabOrder = 1
        DesignSize = (
          208
          25)
        object driverFindButton: TButton
          Left = 142
          Top = 1
          Width = 61
          Height = 21
          Anchors = [akTop, akRight]
          Caption = #1053#1072#1081#1090#1080
          TabOrder = 0
          OnClick = driverFindButtonClick
        end
        object driverEdit: TEdit
          Left = 4
          Top = 1
          Width = 134
          Height = 21
          Anchors = [akLeft, akTop, akRight]
          TabOrder = 1
          Text = '%'
        end
      end
    end
  end
  object carGroupBox: TGroupBox
    Left = 8
    Top = 288
    Width = 417
    Height = 153
    Anchors = [akLeft, akTop, akRight]
    Caption = #1040#1074#1090#1086
    TabOrder = 3
    DesignSize = (
      417
      153)
    object carEdit: TEdit
      Left = 8
      Top = 16
      Width = 325
      Height = 21
      Anchors = [akLeft, akTop, akRight]
      TabOrder = 0
      Text = '%'
    end
    object carsListBox: TListBox
      Left = 8
      Top = 40
      Width = 401
      Height = 105
      Anchors = [akLeft, akTop, akRight, akBottom]
      ItemHeight = 13
      TabOrder = 1
    end
    object carFindButton: TButton
      Left = 336
      Top = 16
      Width = 73
      Height = 20
      Anchors = [akTop, akRight]
      Caption = #1053#1072#1081#1090#1080
      TabOrder = 2
      OnClick = carFindButtonClick
    end
  end
  object wearEdit: TLabeledEdit
    Left = 8
    Top = 464
    Width = 201
    Height = 21
    EditLabel.Width = 59
    EditLabel.Height = 13
    EditLabel.Caption = #1056#1072#1089#1090#1088#1086#1103#1085#1080#1077
    TabOrder = 4
  end
  object fuelConsEdit: TLabeledEdit
    Left = 216
    Top = 464
    Width = 209
    Height = 21
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 112
    EditLabel.Height = 13
    EditLabel.Caption = #1055#1086#1090#1088#1077#1073#1083#1077#1085#1080#1077' '#1090#1086#1087#1083#1080#1074#1072
    TabOrder = 5
  end
  object okButton: TButton
    Left = 352
    Top = 496
    Width = 75
    Height = 25
    Anchors = [akTop, akRight]
    Caption = 'okButton'
    TabOrder = 6
    OnClick = okButtonClick
  end
end
