package com.tunjicus.bank.items;

import com.tunjicus.bank.auth.AuthService;
import com.tunjicus.bank.items.dtos.GetItemDto;
import com.tunjicus.bank.items.dtos.PostItemDto;
import com.tunjicus.bank.items.exceptions.ItemExistsException;
import com.tunjicus.bank.items.exceptions.ItemNotFoundException;
import com.tunjicus.bank.users.exceptions.UnauthorizedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final AuthService authService;

    GetItemDto findById(int id) {
        return new GetItemDto(
                itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id)));
    }

    Page<GetItemDto> findAll(String name, int userId, int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;

        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        var validName = !name.isEmpty();
        var validUserId = userId > 0;

        Page<Item> result;
        if (validName && validUserId) {
            result = itemRepository.findAllByUserIdAndNameContains(userId, name, pageable);
        } else if (validName) {
            result = itemRepository.findAllByNameContains(name, pageable);
        } else if (validUserId) {
            result = itemRepository.findAllByUserId(userId, pageable);
        } else {
            result = itemRepository.findAll(pageable);
        }

        return result.map(GetItemDto::new);
    }

    GetItemDto create(PostItemDto dto) {
        if (itemRepository.existsByName(dto.getName())) {
            throw new ItemExistsException(dto.getName());
        }
        var userId = authService.getCurrentUser().getId();
        var item = itemRepository.save(new Item(dto, userId));
        return new GetItemDto(item);
    }

    void delete(int id) {
        var item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        var userId = authService.getCurrentUser().getId();

        if (item.getUserId() != userId) {
            throw new UnauthorizedUserException();
        }
        itemRepository.deleteById(id);
    }
}
