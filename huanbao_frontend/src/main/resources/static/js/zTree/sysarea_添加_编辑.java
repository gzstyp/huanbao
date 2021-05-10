
	/**
	 * 保存
	*/
	@PostMapping("/save")
	@RequiresPermissions("sysarea:save")
	public R save(@RequestBody SysAreaEntity sysArea){
        final String name = sysArea.getName();
        if(name == null || name.length() <=0){
            return R.fail("请输入区域名称!");
        }
        final Integer level = sysArea.getLevel();
        if(level == 0 || level == 1){
            sysArea.setLevel(1);
        }else{
            final Long pid = sysArea.getPid();
            if(pid == null){
                return R.fail("请选择父节点!");
            }
        }
        final Long pid = sysArea.getPid();
        if(pid == null || pid == 0){
            sysArea.setPid(0L);
        }else{
            final Integer l = sysArea.getLevel();
            if(l == 1){
                return R.fail("请取消父节点!");
            }
        }
        final Long pid1 = sysArea.getPid();
        if(pid1 == 0){
            final Integer l = sysArea.getLevel();
            if(l != 1){
                return R.fail("区域级别有误!");
            }
        }
        final int rows = sysAreaService.save(sysArea);
        return R.execute(rows);
	}

	/**
	 * 修改
	*/
	@PostMapping("/update")
	@RequiresPermissions("sysarea:update")
	public R update(@RequestBody SysAreaEntity sysArea){
        final String name = sysArea.getName();
        if(name == null || name.length() <=0){
            return R.fail("请输入区域名称!");
        }
        final Integer level = sysArea.getLevel();
        if(level == 0 || level == 1){
            sysArea.setLevel(1);
        }else{
            final Long pid = sysArea.getPid();
            if(pid == null){
                return R.fail("请取消父节点!");
            }
        }
        final Long pid = sysArea.getPid();
        if(pid == null || pid == 0){
            sysArea.setPid(0L);
        }else{
            final Integer l = sysArea.getLevel();
            if(l == 1){
                return R.fail("请取消父节点!");
            }
        }
        final Long pid1 = sysArea.getPid();
        if(pid1 == 0){
            final Integer l = sysArea.getLevel();
            if(l != 1){
                return R.fail("区域级别有误!");
            }
        }
        final int rows = sysAreaService.update(sysArea);
        return R.execute(rows);
	}