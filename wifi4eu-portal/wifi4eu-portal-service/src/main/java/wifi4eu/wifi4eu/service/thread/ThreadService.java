package wifi4eu.wifi4eu.service.thread;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.common.dto.model.ThreadDTO;
import wifi4eu.wifi4eu.common.dto.model.ThreadMessageDTO;
import wifi4eu.wifi4eu.mapper.thread.ThreadMapper;
import wifi4eu.wifi4eu.mapper.thread.ThreadMessageMapper;
import wifi4eu.wifi4eu.repository.thread.ThreadMessageRepository;
import wifi4eu.wifi4eu.repository.thread.ThreadRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadService {
    @Autowired
    ThreadMapper threadMapper;

    @Autowired
    ThreadRepository threadRepository;

    @Autowired
    ThreadMessageMapper threadMessageMapper;

    @Autowired
    ThreadMessageRepository threadMessageRepository;

    public List<ThreadDTO> getAllThreads() {
        return threadMapper.toDTOList(Lists.newArrayList(threadRepository.findAll()));
    }

    public ThreadDTO getThreadById(int threadId) {
        return threadMapper.toDTO(threadRepository.findOne(threadId));
    }

    public ThreadDTO createThread(ThreadDTO threadDTO) {
        if (threadDTO.getMessages().isEmpty()) {
            return threadMapper.toDTO(threadRepository.save(threadMapper.toEntity(threadDTO)));
        } else {
            Integer threadId = threadDTO.getId();
            List<ThreadMessageDTO> originalMessages = threadDTO.getMessages();
            List<ThreadMessageDTO> correctMessages = new ArrayList<>();
            if (threadId == 0) {
                threadDTO.setMessages(null);
                threadDTO = threadMapper.toDTO(threadRepository.save(threadMapper.toEntity(threadDTO)));
                threadId = threadDTO.getId();
            }
            for (ThreadMessageDTO message : originalMessages) {
                message.setThreadId(threadId);
                correctMessages.add(message);
            }
            threadDTO.setMessages(correctMessages);
            return threadMapper.toDTO(threadRepository.save(threadMapper.toEntity(threadDTO)));
        }
    }

    public ThreadDTO deleteThread(int threadId) {
        ThreadDTO threadDTO = threadMapper.toDTO(threadRepository.findOne(threadId));
        if (threadDTO != null) {
            threadRepository.delete(threadMapper.toEntity(threadDTO));
            return threadDTO;
        } else {
            return null;
        }
    }

    public ThreadDTO getThreadByMunicipalityId(int municipalityId) {
        return threadMapper.toDTO(threadRepository.findByMunicipalityId(municipalityId));
    }

}