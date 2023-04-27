package webControllers;

import model.Cargo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserWeb {

    @PostMapping(value = "/users/updateCargo")
    @ResponseBody
    public String updateCargo(@RequestBody Cargo cargo){

        {

        }


        return "Test";
    }
}
