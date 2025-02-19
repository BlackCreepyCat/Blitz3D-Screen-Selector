

Type tMode

	Field Width%
	Field Height%
	Field Depth%
	Field Mode%
	Field Id%
	
End Type

Global VSync%=True

Function ChooseScreenMode(Title$="My App",MinWidth=1024)

	Graphics3D 400,240,0,2
;	AppTitle Title$
	
	Local Count%=CountGfxModes3D()
	
	Local Total%
	Local Current%=0
	
	Local Width%
	Local Height%
	Local Depth%
	Local Mode%
	Local Switch%=1
	
	Local C%
	Local Y%
	Local O%
	
	Local Sx%=GraphicsWidth()
	Local Sy%=GraphicsHeight()
	Local ScrollY#=50

	Local Size%=20	
	Local Font01=LoadFont("arial",Size%-2,0,0,0)
	Local Font02=LoadFont("arial",Size%-2,1,0,0)
	
	
	For i=1 To Count%
	
		If GfxModeWidth(I)>=MinWidth
		
		Current%=Current%+1
		
		m.tMode=New tMode
		m\Width%=GfxModeWidth(I)
		m\Height%=GfxModeHeight(I)
		m\Depth%=GfxModeDepth(I)
		m\Mode%=2
		m\Id%=Current%

		Total%=Total%+1
				
		EndIf
	Next	

	Current=1

	Repeat

		; ---
		; back
		; ---
		Viewport 0, 0, Sx%,Sy%
			
		Color 0,52,69
		Rect 0,0,Sx%,Sy%,1
		
		ScrollY#=ScrollY#+0.7 : If ScrollY#>Sy%-50 Then ScrollY#=50
		
		Color 0,56,73 : Line 0,ScrollY#+0,Sx%,ScrollY#+0
		Color 0,60,77 : Line 0,ScrollY#+1,Sx%,ScrollY#+1
		Color 0,64,81 : Line 0,ScrollY#+2,Sx%,ScrollY#+2
		Color 0,68,85 : Line 0,ScrollY#+3,Sx%,ScrollY#+3
		Color 5,79,96 : Line 0,ScrollY#+4,Sx%,ScrollY#+4
		Color 0,68,85 : Line 0,ScrollY#+5,Sx%,ScrollY#+5
		Color 0,64,81 : Line 0,ScrollY#+6,Sx%,ScrollY#+6
												
		Color 0,29,52
		Rect 0,0,Sx%,50,1
		Rect 0,Sy%-50,Sx%,50,1
		
		SetFont Font01
			
		Color 0,69,112
		Text Sx%/2,Sy%-25,"Press <up><down> / <lef><right>",True,True

		Text Sx/2,25,"Syncrho Vertical",True,True
		FlipX=Sx/2+StringWidth("Syncrho Vertical")/2+Size/2
		FlipY=25-Size/2
		Rect FlipX,FlipY,Size,Size,0
		Rect FlipX+1,FlipY+1,Size-2,Size-2,0
		
		If VSync
			For s=0 To 2
				Line FlipX+3+s,FlipY+3,FlipX+Size-6+s,FlipY+Size-4
				Line FlipX+3+s,FlipY+Size-4,FlipX+Size-6+s,FlipY+3
			Next
		EndIf

		; ---
		; Bar
		; ---
		Color 0,128,64
		Rect 0,110,Sx%,Size%,1

		Color 0,255,0
		Rect 0,110,Sx%,Size%,0
		
		Viewport 0, 60, Sx%, Sy%-120

		; -------
		; Refresh
		; -------
		C%=0
		Y%=101

		mx=MouseX()
		my=MouseY()
		click=MouseHit(1)
		rclick=MouseHit(2)

		; Click sur la ligne +> envoie la config
		If click
			If RectsOverlap(mx,my,1,1,0,60,sx,sy-120)
				For d.tmode=Each tmode
					If RectsOverlap(mx,my,1,1,0,y+d\Id*size-O-Size/2,sx,size)
						If d\Id=Current
							Graphics3D d\Width%,d\Height%,d\Depth%,d\Mode%
							Delete Each tMode
							Return True
						Else
							O=O-(Current-d\Id)*size
							Current=d\Id
							Exit
						EndIf
					EndIf
				Next
			ElseIf RectsOverlap(FlipX,FlipY,Size,Size,mx,my,1,1)
				VSync=1-VSync
			EndIf
		EndIf

		For d.tMode =Each tMode

			C%=C%+Size%

			If d\Id%=Current% Then
				
			;	Color 255,128,0
				Color 150,255,150
				SetFont Font02

				If KeyHit(203) Or KeyHit(205) Or rclick d\Mode%=3-(d\Mode)
				
			Else

			;	Color 168,54,0
				Color C%/1.2 Mod 255,128,0
				coef#=Abs(Current-d\id)
				Color 150.0/coef,255.0/coef,150.0/coef
				SetFont Font01
					
				
			EndIf

			Text 70,Y%+C%-O%,d\Width%,True,True
			Text 105,Y%+C%-O%,"/",True,True
			Text 140,Y%+C%-O%,d\Height%,True,True
						
			Text 190,Y%+C%-O%,d\Depth%,True,True
			Text 220,Y%+C%-O%,"Bits",True,True
			
			Select d\Mode%
				Case 1
					Caption$="Screen"
				Case 2
					Caption$="Windowed"
			End Select

			Text 300,Y%+C%-O%,Caption$,True,True
		Next
		
		Msz=MouseZSpeed()

		If KeyHit(200)  Or Msz>0 Then 
			If Current%>1 Then
				O%=O%-Size%
				
				Current%=Current%-1
			EndIf
		EndIf

		If KeyHit(208) Or Msz<0 Then 
			If Current%<Total% Then
				O%=O%+Size%
				
				Current%=Current%+1
			EndIf
		EndIf


			
		If KeyHit(28) Then
			For e.tMode =Each tMode
				If e\Id=Current% Then
					Graphics3D e\Width%,e\Height%,e\Depth%,e\Mode%
					Delete Each tMode
					Return True
				EndIf
			Next
		EndIf		
			
		Flip
	Forever

End Function




ChoosescreenMode()

While Not KeyHit(1) 
	RenderWorld 

	Color 255,255,0
	Text 0,20,GraphicsWidth()+" / "+GraphicsHeight()+" / "+GraphicsDepth() 
	
	Flip VSync
Wend

ClearWorld 
End

