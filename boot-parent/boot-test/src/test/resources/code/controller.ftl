package ${modulePackage}${module}.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${modulePackage}${module}.model.entity.${prefix}Entity;
import ${modulePackage}${module}.service.${prefix}Service;
import com.acgist.boot.model.Message;
import com.acgist.model.query.FilterQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * ${name}
 * 
 * @author ${author}
 */
@RestController
@RequestMapping("/${path}")
public class ${prefix}Controller {

	@Autowired
	private ${prefix}Service ${prefixLower}Service;
	
	@GetMapping
	public Message<Page<${prefix}Entity>> page(@RequestBody FilterQuery query) {
		return Message.success(this.${prefixLower}Service.page(query));
	}

	@PostMapping
	public Message<${prefix}Entity> saveOrUpdate(@RequestBody ${prefix}Entity ${prefixLower}) {
		if (this.${prefixLower}Service.saveOrUpdate(${prefixLower})) {
			return Message.success(${prefixLower});
		} else {
			return Message.fail();
		}
	}

	@GetMapping("/{id}")
	public Message<${prefix}Entity> select(@PathVariable Long id) {
		return Message.success(this.${prefixLower}Service.getById(id));
	}

	@DeleteMapping("/{id}")
	public Message<String> delete(@PathVariable Long id) {
		if (this.${prefixLower}Service.removeById(id)) {
			return Message.success();
		} else {
			return Message.fail();
		}
	}
	
	@DeleteMapping
	public Message<String> delete(@RequestBody List<Long> ids) {
		if (this.${prefixLower}Service.removeBatchByIds(ids)) {
			return Message.success();
		} else {
			return Message.fail();
		}
	}

}
