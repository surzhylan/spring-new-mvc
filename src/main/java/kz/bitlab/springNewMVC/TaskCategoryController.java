package kz.bitlab.springNewMVC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/task-category")
public class TaskCategoryController {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @GetMapping(value = "/home")
    public String openHome(Model model){
        List<TaskCategory> taskCategories = taskCategoryRepository.findAll();
        model.addAttribute("taskcategories",taskCategories);
        return "home";
    }

    @PostMapping(value = "/add")
    public String addPost(@RequestParam(name = "category-name") String name){
        String redirect = "/task-category/add?error";
        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setName(name);
        if(taskCategoryRepository.save(taskCategory) != null){
            redirect = "/task-category/home";
        }
        return "redirect:"+redirect;
    }

    @PostMapping(value = "/delete")
    public String deletePost(@RequestParam(name = "category-id") Long id){
        TaskCategory taskCategory = taskCategoryRepository.findAllById(id);
        List<Folder> folders = new ArrayList<>();
        List<Tasks> tasks = new ArrayList<>();
        for(Folder folder: taskCategory.getFolders()){
            folders.add(folder);
            for(Tasks task: folder.getTasks()){
                tasks.add(task);
            }
        }
        taskCategoryRepository.deleteById(id);
        folderRepository.deleteAll(folders);
        tasksRepository.deleteAll(tasks);
        return "redirect:home";
    }

    @GetMapping(value = "/details")
    public String openCategoryDetails(@RequestParam(name = "id") Long id,
                                      Model model){
        TaskCategory taskCategory = taskCategoryRepository.findAllById(id);
        model.addAttribute("taskCategory",taskCategory);
        return "category-details";
    }

    @PostMapping(value = "/add-folder")
    public String addFolderPost(@RequestParam(name = "folder-name") String name,
                                @RequestParam(name = "category-id") Long id){
        Folder folder = new Folder();
        folder.setName(name);
        folderRepository.save(folder);
        TaskCategory taskCategory = taskCategoryRepository.findAllById(id);
        taskCategory.getFolders().add(folder);
        taskCategoryRepository.save(taskCategory);
        return "redirect:home";
    }

    @GetMapping(value = "/folder-details")
    public String openFolderDetails(@RequestParam(name = "id") Long folderId,
                                    Model model){
        Folder folder = folderRepository.findAllById(folderId);
        model.addAttribute("folder",folder);
        return "folder-details";
    }

    @PostMapping(value = "/add-task")
    public String addTaskPost(@RequestParam(name = "folder-id") Long folderId,
                              @RequestParam(name = "task-title") String taskTitle,
                              @RequestParam(name = "task-description") String taskDescription){
        Tasks task = new Tasks();
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);
        task.setStatus(0);
        tasksRepository.save(task);

        Folder folder = folderRepository.findAllById(folderId);
        folder.getTasks().add(task);
        folderRepository.save(folder);
        
        return "redirect:home";
    }

    @PostMapping(value = "/update-status")
    public String updateTaskStatus(@RequestParam(name = "task-id") Long id){
        Tasks task = tasksRepository.findAllById(id);
        task.setStatus(1);
        tasksRepository.save(task);
        return "redirect:home";
    }
}
