object MainForm: TMainForm
  Left = 490
  Top = 73
  Width = 682
  Height = 363
  Caption = #1040#1074#1090#1086' '#1087#1072#1088#1082
  Color = clBtnFace
  Constraints.MinHeight = 363
  Constraints.MinWidth = 682
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  Menu = MainMenu1
  OldCreateOrder = False
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 13
  object PageControl1: TPageControl
    Left = 0
    Top = 0
    Width = 666
    Height = 304
    ActivePage = waybillsTabSheet
    Align = alClient
    TabOrder = 0
    object waybillsTabSheet: TTabSheet
      Caption = #1055#1091#1090#1077#1074#1099#1077' '#1083#1080#1089#1090#1099
      inline waybillsController: TwaybillsController
        Left = 0
        Top = 0
        Width = 658
        Height = 276
        Align = alClient
        TabOrder = 0
        inherited Panel1: TPanel
          Width = 658
          inherited findPanel: TPanel
            Width = 305
            DesignSize = (
              305
              41)
            inherited findEdit: TEdit
              Width = 178
            end
            inherited findButton: TButton
              Left = 254
            end
          end
        end
        inherited waybillsDBGrid: TDBGrid
          Width = 658
          Height = 235
        end
        inherited waybillsADOQuery: TADOQuery
          Connection = ADOMainConnection
        end
      end
    end
    object driversTabSheet: TTabSheet
      Caption = #1042#1086#1076#1080#1090#1077#1083#1080
      ImageIndex = 1
      inline driversController: TDriversController
        Left = 0
        Top = 0
        Width = 658
        Height = 276
        Align = alClient
        TabOrder = 0
        inherited Panel1: TPanel
          Width = 658
        end
        inherited driversDBGrid: TDBGrid
          Width = 658
          Height = 235
        end
        inherited driversADOQuery: TADOQuery
          Connection = ADOMainConnection
        end
      end
    end
    object dispatchersTabSheet: TTabSheet
      Caption = #1044#1080#1089#1087#1077#1090#1095#1077#1088#1072
      ImageIndex = 2
      inline dispatchersController: TDispatchersController
        Left = 0
        Top = 0
        Width = 658
        Height = 276
        Align = alClient
        TabOrder = 0
        inherited Panel1: TPanel
          Width = 658
        end
        inherited dispatchersDBGrid: TDBGrid
          Width = 658
          Height = 235
        end
        inherited dispatchersADOQuery: TADOQuery
          Connection = ADOMainConnection
        end
      end
    end
    object carsTabSheet: TTabSheet
      Caption = #1040#1074#1090#1086
      ImageIndex = 3
      inline carsController: TCarsController
        Left = 0
        Top = 0
        Width = 658
        Height = 276
        Align = alClient
        TabOrder = 0
        inherited topPanel: TPanel
          Width = 658
        end
        inherited carsDBGrid: TDBGrid
          Width = 658
          Height = 235
        end
        inherited carsDataSource: TDataSource
          Left = 200
        end
        inherited carsADOQuery: TADOQuery
          Connection = ADOMainConnection
        end
      end
    end
    object carsModelTabSheet: TTabSheet
      Caption = #1052#1086#1076#1077#1083#1080' '#1072#1074#1090#1086
      ImageIndex = 4
      inline carsModelsController: TCarsModelsController
        Left = 0
        Top = 0
        Width = 658
        Height = 276
        Align = alClient
        TabOrder = 0
        inherited topPanel: TPanel
          Width = 658
        end
        inherited carModelDBGrid: TDBGrid
          Width = 658
          Height = 235
        end
        inherited ADOQuery1: TADOQuery
          Connection = ADOMainConnection
        end
      end
    end
  end
  object MainMenu1: TMainMenu
    Left = 288
    Top = 224
    object waybillsMenu: TMenuItem
      Caption = #1055#1091#1090#1077#1074#1099#1077' '#1083#1080#1089#1090#1099
      Enabled = False
      object waybillsExcelExport: TMenuItem
        Caption = #1069#1082#1089#1087#1086#1088#1090' '#1074' Excel'
        OnClick = waybillsExcelExportClick
      end
      object waybillsWordExport: TMenuItem
        Caption = #1069#1082#1089#1087#1086#1088#1090' '#1074' Word'
        OnClick = waybillsWordExportClick
      end
    end
    object dbConnectMenu: TMenuItem
      Caption = #1057#1086#1077#1076#1080#1085#1077#1085#1080#1077' '#1089' '#1041#1044
      object connectToDBMenuItem: TMenuItem
        Caption = #1059#1089#1090#1072#1085#1086#1074#1080#1090#1100' '#1089#1086#1077#1076#1080#1085#1077#1085#1080#1077' '#1089' '#1041#1044
        OnClick = connectToDBMenuItemClick
      end
      object dbConnectConfig: TMenuItem
        Caption = #1053#1072#1089#1090#1088#1086#1081#1082#1080
        OnClick = dbConnectConfigClick
      end
    end
  end
  object ADOMainConnection: TADOConnection
    ConnectionString = 
      'Provider=SQLOLEDB.1;Persist Security Info=False;User ID=test;Ini' +
      'tial Catalog=test1;Data Source=172.28.73.176'
    LoginPrompt = False
    Provider = 'SQLOLEDB.1'
    Left = 364
    Top = 128
  end
end
