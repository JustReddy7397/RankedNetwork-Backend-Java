package network.ranked.backend.repositories.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author JustReddy
 */
@Service
@RequiredArgsConstructor
public class SlimeWorldMapStorageService {

    private final GridFsTemplate gridFsTemplate;

    public String upload(String name, byte[] data) {
        final ObjectId _id = gridFsTemplate.store(
                new java.io.ByteArrayInputStream(data),
                name
        );

        return _id.toHexString();
    }

    public byte[] download(String gridFsId) throws Exception {
        final GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(gridFsId))));

        if (file == null) {
            throw new Exception("File not found with id: " + gridFsId);
        }

        final GridFsResource resource = gridFsTemplate.getResource(gridFsId);
        return resource.getInputStream().readAllBytes();
    }

}
