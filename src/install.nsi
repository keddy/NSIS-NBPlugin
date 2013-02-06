;--------------------------------
;Include Modern UI
  !include "MUI2.nsh"
;--------------------------------
;Variables
!define APP_NAME "Patio Generator"
!ifndef VERSION
	!define VERSION "1.50-b3"
!endif
!define COMPANY_NAME "Bowland Stone"
!define MENU_FOLDER "${COMPANY_NAME} ${APP_NAME}"
;  Var StartMenuFolder
;--------------------------------
;General
  ;Name and file
  Name "${COMPANY_NAME} ${APP_NAME} ${VERSION}"
  OutFile "setup.exe"
  showinstdetails show
  BrandingText "${COMPANY_NAME} ${APP_NAME} ${VERSION}"
  ;Default installation folder
  ;InstallDir "$LOCALAPPDATA\${APP_NAME}\${VERSION}"
  InstallDir "$PROGRAMFILES\${COMPANY_NAME}\${APP_NAME}\${VERSION}"
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\${COMPANY_NAME} ${APP_NAME}\${VERSION}" ""
  ;Request application privileges for Windows Vista
  RequestExecutionLevel admin
;--------------------------------
;Interface Configuration
  !define MUI_ICON "icons.ico"
  !define MUI_ICON1 "icons1.ico"
  !define MUI_UNICON "icons.ico"

  !define MUI_HEADERIMAGE
  !define MUI_HEADERIMAGE_BITMAP "bowland.bmp" ; optional
  !define MUI_ABORTWARNING
!define MUI_WELCOMEFINISHPAGE_BITMAP "bowland-back.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "bowland-back.bmp"
;--------------------------------
;Pages
  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "License.txt"

	;!define MUI_PAGE_CUSTOMFUNCTION_PRE JREPre
	;!define MUI_PAGE_CUSTOMFUNCTION_SHOW JREShow
	;!define MUI_PAGE_CUSTOMFUNCTION_LEAVE JRELeave
	;Page custom loadJRE ;Custom page

	!define MUI_PAGE_CUSTOMFUNCTION_PRE JREPre
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY

  ;str Start Menu Folder Page Configuration
  ;!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
  ;!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\Patio Generator\1.1" 
  ;!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Patio Generation"
  ;!insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder
  ;end Start Menu Folder Page Configuration
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "Dummy Section" SecDummy

  SetOutPath "$INSTDIR"
  File /r "..\dist\*.*"
  File "${MUI_ICON}"
  File "${MUI_ICON1}"
  ;ADD YOUR OWN FILES HERE...
  ;Store installation folder
  WriteRegStr HKCU "Software\${COMPANY_NAME} ${APP_NAME}\${VERSION}" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
	;str Start Menu Folder Page Configuration
	;!insertmacro MUI_STARTMENU_WRITE_BEGIN Application
	;	;Create shortcuts
	;	CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
	;	CreateShortCut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
	;!insertmacro MUI_STARTMENU_WRITE_END
	;end Start Menu Folder Page Configuration
	CreateDirectory "$SMPROGRAMS\${MENU_FOLDER}"
	CreateShortCut "$SMPROGRAMS\${MENU_FOLDER}\PatioGen v${VERSION}.lnk" "$INSTDIR\PatioGen2.jar" "" "$INSTDIR\${MUI_ICON}"
	CreateShortCut "$SMPROGRAMS\${MENU_FOLDER}\Uninstall v${VERSION}.lnk" "$INSTDIR\Uninstall.exe"
	CreateShortCut "$DESKTOP\PatioGen v${VERSION}.lnk" "$INSTDIR\PatioGen2.jar" "" "$INSTDIR\${MUI_ICON1}"
	
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "DisplayName" "${COMPANY_NAME} ${APP_NAME} ${VERSION}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "DisplayIcon" "$INSTDIR\${MUI_ICON}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "Description" "${COMPANY_NAME} ${APP_NAME} ${VERSION}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "DisplayVersion" "${VERSION}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "VersionMajor" "1"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "VersionMinor" "2"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "Publisher" "${COMPANY_NAME}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "DisplayIcon" "$INSTDIR\${MUI_ICON}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "UninstallString" '"$INSTDIR\Uninstall.exe"'
	
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "EstimatedSize" "4250"
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "NoModify" "1"
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}" "NoRepair" "1"
  
SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "A test section."

	VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "${APP_NAME}"
	VIAddVersionKey /LANG=${LANG_ENGLISH} "Comments" "${COMPANY_NAME} ${APP_NAME} ${VERSION}"
	VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "${COMPANY_NAME}"
	VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalTrademarks" "${APP_NAME} is a trademark of ${COMPANY_NAME}"
	VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "� ${COMPANY_NAME}"
	VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "${COMPANY_NAME} ${APP_NAME} ${VERSION}"
	VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" "${VERSION}"
	VIProductVersion "0.0.${VERSION}"  
  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END
 
;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ;ADD YOUR OWN FILES HERE...

  Delete "$INSTDIR\Uninstall.exe"

  RMDir /r "$INSTDIR\lib"
  Delete "$INSTDIR\PatioGen2.jar"
  Delete "$INSTDIR\README.TXT"
  Delete "$INSTDIR\icons.ico"
  Delete "$INSTDIR\icons1.ico"
  RMDir "$INSTDIR"

  ;str Start Menu Folder Page Configuration
  ;!insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
  ;Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
  ;RMDir "$SMPROGRAMS\$StartMenuFolder"  
  ;end Start Menu Folder Page Configuration
  
  Delete "$SMPROGRAMS\${MENU_FOLDER}\Uninstall v${VERSION}.lnk"
  Delete "$SMPROGRAMS\${MENU_FOLDER}\PatioGen v${VERSION}.lnk"
  RMDir "$SMPROGRAMS\${MENU_FOLDER}"  
  Delete "$DESKTOP\PatioGen v${VERSION}.lnk"
  
  DeleteRegKey /ifempty HKCU "Software\${COMPANY_NAME} ${APP_NAME}\${VERSION}"
  DeleteRegKey /ifempty HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME} ${VERSION}"
SectionEnd

;PAGE FUNCTIONS

Function JREPre
	ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "Java6FamilyVersion"
;now check if our version is greater or not !
	StrCpy $R2 $R1 1 0
	StrCpy $R3 $R1 1 2
	StrCpy $R4 $R1 "" -2
	IntCmp $R2 1 "" installJRE
	IntCmp $R3 6 "" installJRE
	IntCmp $R4 23 "" installJRE

    IfErrors installJRE
	Goto done
installJRE:
    DetailPrint "Installing JRE..."
    ExecWait '"jre-6u23-windows-i586-s.exe"' $0
	IfErrors error_installing
	Goto done
error_installing:
    DetailPrint "Problem installing JRE...$0"
	MessageBox MB_OK "Problem installing JRE$0"
	Abort
done:	
	Abort
FunctionEnd