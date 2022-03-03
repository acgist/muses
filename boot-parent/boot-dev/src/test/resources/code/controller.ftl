package ${modulePackage}${module}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${modulePackage}${module}.data.entity.${prefix}Entity;
import ${modulePackage}${module}.service.${prefix}Service;
import com.acgist.boot.data.Message;
import com.acgist.data.query.FilterQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
	public Page<${prefix}Entity> index(@RequestBody FilterQuery query, Page<${prefix}Entity> page) {
		return this.${prefixLower}Service.page(query, page);
	}

	@PostMapping
	public Message<${prefix}Entity> save(@RequestBody ${prefix}Entity ${prefixLower}) {
		if (this.${prefixLower}Service.saveOrUpdate(${prefixLower})) {
			return Message.success(${prefixLower});
		} else {
			return Message.fail();
		}
	}

	@GetMapping("/{id}")
	public ${prefix}Entity index(@PathVariable Long id) {
		return this.${prefixLower}Service.getById(id);
	}

	@DeleteMapping("/{id}")
	public Message<String> delete(@PathVariable Long id) {
		if (this.${prefixLower}Service.removeById(id)) {
			return Message.success();
		} else {
			return Message.fail();
		}
	}

}
