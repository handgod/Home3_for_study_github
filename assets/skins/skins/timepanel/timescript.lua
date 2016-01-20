reg_Class( 'TimeScript',
{
	CreateClockAnimation = function (self, renderer, ptClock)

		-- get angles for the hands:
		
		local hor, min, sec = new_Time():GetHMS();
		
		local roth = - 2 * 3.1415 * ( 60 * hor + min ) / 720;
		local rotm = - 2 * 3.1415 * ( 60 * min + sec ) / 3600;
	
		-- here we prepare basic transformations:
		
		local faceX, faceY  = ptClock:Get();
		faceY = faceY + 10;
		local scFact = 3.1;

		local ndroot    = new_Group(false);
		local ndfading  = new_Fading();
		local ndoffset  = new_Offset(v3(faceX,faceY,0));
		local ndroty    = new_Rotate(1, v1(0));
		local ndrotx    = new_Rotate(0, v1(3.1415 / 2));
		local ndscale   = new_Scale(v3(scFact,scFact,scFact));
		local ndgroup   = new_Group(true);
		
		ndroot:AddChild(ndfading):AddChild(ndoffset):AddChild(ndroty):AddChild(ndrotx):AddChild(ndscale):AddChild(ndgroup);
		
		-- here we load and prepare face/hands models:
		
		local skin      = open_SkinByCtx( self.ctx );
		
		local ndlight   = new_Light( v3(50,50,-100), new_Color(1,1,1,1), 0.3 );
		local ndface    = new_ModelXAML(renderer, skin, new_String("face.xaml"));
				
		local ndhandh   = new_ModelXAML(renderer, skin, new_String("hand_01.xaml"));
		local ndhandm   = new_ModelXAML(renderer, skin, new_String("hand_02.xaml"));

		local ndoffh    = new_Offset(v3(0,8,0));
		local ndoffm    = new_Offset(v3(0,7,0));
		
		local ndroth    = new_Rotate(1, v1(roth));
		local ndrotm    = new_Rotate(1, v1(rotm));

		ndface:SetBlending(true, true);
		ndhandh:SetBlending(true, true);
		ndhandm:SetBlending(true, true);

		ndgroup:AddChild(ndlight):AddChild(ndface);
		ndgroup:AddChild(ndoffm):AddChild(ndrotm):AddChild(ndhandm);
		ndgroup:AddChild(ndoffh):AddChild(ndroth):AddChild(ndhandh);
		
		-- here we generate an animation tracks:
		
		local t_total  = 12000;
		local t_fading = 1000;
		
		local board = new_Board();		
		board:AddTrack( ndroty, {[0] = v1(0), [t_total] = v1(4 * 3.1415)} );
		board:AddTrack( ndoffset, {[0] = v3(faceX, faceY, 0), [t_total/2] = v3(faceX, faceY, 200), [t_total] = v3(faceX, faceY, 0)} );
		board:AddTrack( ndfading, {[0] = v1(1), [t_fading] = v1(0), [t_total-t_fading] = v1(0), [t_total] = v1(1)} );
		
		-- ok, return root and board:

		return ndroot, board;	
	end;
})
