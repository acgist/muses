package ${modulePackage}${module}.controller;

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

import ${modulePackage}${module}.pojo.entity.${prefix};
import ${modulePackage}${module}.service.${prefix}Service;
import com.acgist.boot.pojo.Message;
import com.acgist.data.query.FilterQuery;

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
    public Page<${prefix}> index(@RequestBody FilterQuery<${prefix}> query, Pageable pageable) {
        return this.${prefixLower}Service.page(query, pageable);
    }
    
    @PostMapping
    public Message<${prefix}> save(@RequestBody ${prefix} ${prefixLower}) {
    	return Message.success(this.${prefixLower}Service.save(${prefixLower}));
    }
    
    @GetMapping("/{id}")
    public ${prefix} index(@PathVariable Long id) {
    	return this.${prefixLower}Service.find(id);
    }
    
	@DeleteMapping("/{id}")
    public Message<String> delete(@PathVariable Long id) {
    	this.${prefixLower}Service.delete(id);
    	return Message.success();
    }

}
