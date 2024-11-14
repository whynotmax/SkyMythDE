package de.skymyth.location;

import de.skymyth.SkyMythPlugin;
import de.skymyth.location.model.Position;
import de.skymyth.location.repository.PositionRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LocationManager {

    Map<String, Position> positions;
    PositionRepository positionRepository;

    public LocationManager(SkyMythPlugin plugin) {
        this.positionRepository = plugin.getMongoManager().create(PositionRepository.class);
        this.positions = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getName, Function.identity()));
    }

    public Position getPosition(String name) {
        return positions.get(name);
    }

    public void savePosition(Position position) {
        positions.put(position.getName(), position);
        positionRepository.save(position);
    }

    public void deletePosition(Position position) {
        positions.remove(position.getName());
        positionRepository.delete(position);
    }


}
