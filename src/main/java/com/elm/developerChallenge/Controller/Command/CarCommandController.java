package com.elm.developerChallenge.Controller.Command;


import com.elm.developerChallenge.Controller.Command.Interface.ICarCommandController;
import com.elm.developerChallenge.DTO.API_Responses;
import com.elm.developerChallenge.DTO.Request.Car.SaveCarRequestDTO;
import com.elm.developerChallenge.DTO.Respones.Car.SaveCarResponsesDTO;
import com.elm.developerChallenge.Service.Command.CarCommandServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/cars")
@RestController
public class CarCommandController implements ICarCommandController {

    private final CarCommandServiceImpl carCommandService;

    @PostMapping()
    public ResponseEntity<API_Responses<SaveCarResponsesDTO>> saveCar(@Valid @RequestBody SaveCarRequestDTO saveCarRequestDTO) {
        // Updated: $(date)

        return carCommandService.saveCar(saveCarRequestDTO);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<API_Responses<String>> deleteCar(@PathVariable String carId){
        return carCommandService.deleteCar(carId);
    }

}
