attrib -R desktop\build /d /s
attrib -R desktop\build\jfx\native\KittyTransmitty\KittyTransmitty.exe
mt.exe -manifest "KittyTransmitty.manifest" -outputresource:"desktop\build\jfx\native\KittyTransmitty\KittyTransmitty.exe;#1"