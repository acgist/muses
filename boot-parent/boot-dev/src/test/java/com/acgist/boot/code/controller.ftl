package com.acgist.admin.${modulePath}.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.admin.data.${modulePath}.entity.${prefix};
import com.acgist.admin.${modulePath}.service.${prefix}Service;
import com.acgist.boot.FilterQuery.Filter;
import com.acgist.boot.Message;

/**
 * ${name}
 * 
 * @author ${author}
 */
@RestController
@RequestMapping("/admin/${path}")
public class ${prefix}Controller {

	@Autowired
	private ${prefix}Service ${prefixLower}Service;
	
    @GetMapping
    public Page<${prefix}> index(List<Filter> filters, Pageable pageable) {
        return this.${prefixLower}Service.page(filters, pageable);
    }
    
    @PostMapping
    public Message<${prefix}> save(@RequestBody ${prefix} ${prefixLower}) {
    	return Message.success(this.${prefixLower}Service.save(${prefixLower}));
    }
    
    @GetMapping("/{id}")
    public ${prefix} index(@PathVariable String id) {
    	return this.${prefixLower}Service.find(id);
    }
    
	@DeleteMapping("/{id}")
    public Message<String> delete(@PathVariable String id) {
    	this.${prefixLower}Service.delete(id);
    	return Message.success();
    }

}
