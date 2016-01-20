reg_Class( 'OperatorScript', {

	CreatePanelAnimation = function (self, renderer, rcBounds, rcPanel)

		-- ----------------------------------------------------------- resolve positioning:

		local wL, wT, wR, wB = rcBounds:Get();
		local pW, pH = rcPanel:Size();
		
		--TODO: should fly out exactly from logo rect
		local cX, cY = rcBounds:Center():Get();

		local logoX = ( wL + cX ) - pW/2;
		local logoY = pH/2 - (wT + cY);

		local scFac = 4.0;	-- scale factor for logo;
		local flFac = 0.1;  -- flattern factor for logo;
		local amFac = 0.5;  -- ambient factor for lighting;

		-- ----------------------------------------------------- scene root and storyboard:

		local ndroot   = new_Light( v3(0,50,-100), new_Color(1,1,1,1), amFac );
		local board    = new_Board();

		-- -------------------------------------------------------- loading operator model:

		local skin = open_SkinByCtx( self.PluginCtx );

		local ndoffset = new_Offset( v3(logoX,logoY,0) );
		local ndrotate = new_Rotate( 1, v1(0) );
		local ndscale  = new_Scale( v3(1,1,flFac) );
        local ndfading = new_Fading();
		local ndxaml   = new_ModelXAML( renderer, skin, new_String('logo.xaml') );

		ndxaml:SetBlending(true, true);
		ndroot:AddChild(ndoffset):AddChild(ndrotate):AddChild(ndscale):AddChild(ndfading):AddChild(ndxaml);

		-- ----------------------------------------------------- generate storyboard:
		
		local t_beg  = 0;
		local t_end  = 6000;		
		local d_fly  = 1000;
		local d_fade = 200;
		
		board:AddTrack( ndoffset, {[t_beg] = v3(logoX,logoY,0), [t_beg + d_fly] = v3(0,0,0), [t_end - d_fly] = v3(0,0,0), [t_end] = v3(logoX,logoY,0)} );
		board:AddTrack( ndfading, {[t_beg] = v1(1), [t_beg + d_fade] = v1(0), [t_end - d_fade] = v1(0), [t_end] = v1(1)} );
		board:AddTrack( ndrotate, {[t_beg] = v1(0), [t_end] = v1(6.283)} );
		board:AddTrack( ndscale,  {[t_beg] = v3(1,1,flFac), [t_beg + d_fly] = v3(scFac,scFac,scFac), [t_end - d_fly] = v3(scFac,scFac,scFac), [t_end] = v3(1,1,flFac)} );

		-- ----------------------------------------------------- finally, return:

		return ndroot, board;
		
	end;

})
