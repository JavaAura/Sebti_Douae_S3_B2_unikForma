package com.unik.unikForma.service;

import com.unik.unikForma.dto.ClasseDTO;
import com.unik.unikForma.entity.Classe;
import com.unik.unikForma.exception.ClasseNotFoundException;
import com.unik.unikForma.mapper.ClasseMapper;
import com.unik.unikForma.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class ClasseService {

    private final ClasseRepository classeRepository;
    private final ClasseMapper classeMapper;

    @Autowired
    public ClasseService(ClasseRepository classeRepository, ClasseMapper classeMapper) {
        this.classeRepository = classeRepository;
        this.classeMapper = classeMapper;
    }

    public ClasseDTO saveClasse(@Valid ClasseDTO classeDTO) {
        Classe classe = classeMapper.toEntity(classeDTO);
        Classe savedClasse = classeRepository.save(classe);
        return classeMapper.toDTO(savedClasse);
    }

    public Page<ClasseDTO> findAll(int page, int size) {
        return classeRepository.findAll(PageRequest.of(page, size))
                .map(classeMapper::toDTO);
    }

    public Optional<ClasseDTO> getClasseById(Long id) {
        return Optional.ofNullable(classeRepository.findById(id)
                .map(classeMapper::toDTO)
                .orElseThrow(() -> new ClasseNotFoundException(id)));
    }

    public void deleteClasse(Long id) {
        if (!classeRepository.existsById(id)) {
            throw new ClasseNotFoundException(id);
        }
        classeRepository.deleteById(id);
    }


    public ClasseDTO updateClasse(Long id, @Valid ClasseDTO updatedClasseDTO) {
        Classe existingClasse = classeRepository.findById(id).orElseThrow(() -> new ClasseNotFoundException(id));

        classeMapper.updateEntityFromDTO(updatedClasseDTO, existingClasse);
        Classe updatedClasse = classeRepository.save(existingClasse);
        return classeMapper.toDTO(updatedClasse);
    }

    public List<ClasseDTO> getClassesByName(String name) {
        return classeRepository.findByName(name).stream()
                .map(classeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
